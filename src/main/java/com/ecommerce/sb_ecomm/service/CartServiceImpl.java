package com.ecommerce.sb_ecomm.service;

import com.ecommerce.sb_ecomm.exception.APIException;
import com.ecommerce.sb_ecomm.exception.ResourceNotFoundException;
import com.ecommerce.sb_ecomm.model.Cart;
import com.ecommerce.sb_ecomm.model.CartItem;
import com.ecommerce.sb_ecomm.model.Product;
import com.ecommerce.sb_ecomm.model.Users;
import com.ecommerce.sb_ecomm.payload.CartDTO;
import com.ecommerce.sb_ecomm.payload.ProductDTO;
import com.ecommerce.sb_ecomm.repository.CartItemRepository;
import com.ecommerce.sb_ecomm.repository.CartRepository;
import com.ecommerce.sb_ecomm.repository.ProductRepository;
import com.ecommerce.sb_ecomm.util.AuthUtil;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    CartRepository cartRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CartItemRepository cartItemRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    AuthUtil authUtil;


    @Override
    public CartDTO addProductToCart(Long productId, Integer quantity) {
        // Find existing cart or create one
        Cart cart = createCart();

        // Retrieve Product Details
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        // Perform Validations
        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(
                cart.getCartId(),
                productId
        );

        if (cartItem != null) {
            throw new APIException("Product " + product.getProductName() + " already exists in Cart");
        }

        if (product.getQuantity() == 0) {
            throw new APIException(product.getProductName() + " is not available");
        }

        if (product.getQuantity() < quantity) {
            throw new APIException("Please, make and order of the " + product.getProductName() + " less than or equal to the quantity " + product.getQuantity());
        }

        // Create Cart Item
        CartItem newCartItem = new CartItem();

        newCartItem.setProduct(product);
        newCartItem.setCart(cart);
        newCartItem.setQuantity(quantity);
        newCartItem.setDiscount((product.getDiscount()));
        newCartItem.setProductPrice(product.getSpecialPrice());
        cartItemRepository.save(newCartItem);

        product.setQuantity(product.getQuantity());

        cart.setTotalPrice(cart.getTotalPrice() + (product.getSpecialPrice() * quantity));

        // Save Cart Item
        cartRepository.save(cart);

        // Return Updated Cart
        CartDTO cartDTO = modelMapper.map(newCartItem, CartDTO.class);

        List<CartItem> cartItemList = cart.getCartItems();

        Stream<ProductDTO> productDTOStream = cartItemList.stream().map(item -> {
            ProductDTO map = modelMapper.map(item.getProduct(), ProductDTO.class);
            map.setQuantity(item.getQuantity());
            return map;
        });

        cartDTO.setProducts(productDTOStream.toList());
        return cartDTO;
    }


    private Cart createCart() {
        Cart userCart = cartRepository.findCartByEmail(authUtil.loggedInEmail());
        if (userCart != null) {
            return userCart;
        }

        Cart cart = new Cart();
        cart.setTotalPrice(0.00);
        cart.setUser(authUtil.loggedInUser());
        Cart newCart = cartRepository.save(cart);
        return newCart;
    }

    @Override
    public List<CartDTO> getAllCarts() {
        List<Cart> cartList = cartRepository.findAll();
        if (cartList.size() == 0) {
            throw new APIException("No cart exist");
        }

        List<CartDTO> cartDTOS = cartList.stream()
                .map(cart -> {
                    CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
                    // in cart DTO there are Product DTO
                    List<ProductDTO> products = cart.getCartItems().stream().map(
                            p -> modelMapper.map(p, ProductDTO.class)
                    ).collect(Collectors.toList());

                    cartDTO.setProducts(products);
                    return cartDTO;
                }).collect(Collectors.toList());
        return cartDTOS;
    }

    @Override
    public CartDTO getCart(String emailId, Long cartId) {
        Cart cart = cartRepository.findCartByEmailAndCartId(emailId, cartId);
        if (cart == null) {
            throw new ResourceNotFoundException("Cart", "cartId", cartId);
        }
        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
//        cart.getCartItems().forEach(c -> c.getProduct().setQuantity(c.getQuantity()));
        List<ProductDTO> productDTOS = cart.getCartItems().stream().map(p ->
                        modelMapper.map(p.getProduct(), ProductDTO.class))
                .collect(Collectors.toList());
        cartDTO.setProducts(productDTOS);
        return cartDTO;
    }

    @Override
    @Transactional // Doing Multiple operation issue in third operation other 2 operations will be rolled backed, all are done and none will done
    public CartDTO updateProductQuantityInCart(Long productId, Integer quantity) {
        String email = authUtil.loggedInEmail();
        Cart userCart = cartRepository.findCartByEmail(email);
        Long cartId = userCart.getCartId();

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        if (product.getQuantity() == 0) {
            throw new APIException(product.getProductName() + " is not available");
        }

        if (product.getQuantity() < quantity) {
            throw new APIException("Please, make and order of the " + product.getProductName() + " less than or equal to the quantity " + product.getQuantity());
        }

        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(productId, cartId);
        if(cartItem == null) {
            throw new APIException("Product "+product.getProductName()+" doesn't exist");
        }

        int newQantity = cartItem.getQuantity() + quantity;
        if(newQantity < 0) {
            throw new APIException("The recalculation of the quantity can not negative");
        }

        if(newQantity == 0 ) {
            deleteProductFromCart(productId, cartId);
        } else {
            cartItem.setProductPrice(product.getSpecialPrice());
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            cartItem.setDiscount((product.getDiscount()));
            cart.setTotalPrice(cart.getTotalPrice() + (cartItem.getProductPrice() * quantity));
            cartRepository.save(cart);
        }

        CartItem updatedCartItem = cartItemRepository.save(cartItem);

        if(updatedCartItem.getQuantity() == 0) {
            cartItemRepository.deleteById(updatedCartItem.getCartItemId());
        }

        CartDTO cartDTO = modelMapper.map(cart,  CartDTO.class);
        Stream<ProductDTO> productDTOStream = cart.getCartItems().stream().map( item -> {
            ProductDTO productDTO = modelMapper.map(item.getProduct(), ProductDTO.class);
            productDTO.setQuantity(item.getQuantity());
            return productDTO;
                });
        cartDTO.setProducts(productDTOStream.toList());
        return cartDTO;
    }

    @Transactional
    @Override
    public String deleteProductFromCart(Long cartId, Long productId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));

        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(productId, cartId);

        if(cartItem == null) {
            throw new ResourceNotFoundException("Product", "productId", productId);
        }

        cart.setTotalPrice(cart.getTotalPrice() - (cartItem.getProductPrice() * cartItem.getQuantity()));

        cartItemRepository.deleteCartItemByProductIdAndCartId(cartId, productId);

        return "Product " + cartItem.getProduct().getProductName() + " removed from cart!!";
    }

    @Override
    public void updateProductInCarts(Long cartId, Long productId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(productId, cartId);

        if(cartItem == null) {
            throw new APIException("product " + product.getProductName() + " not available in the cart!!");
        }

        // 1000 - 100*2 = 800
        double cartPrice = cart.getTotalPrice() - (cartItem.getProductPrice() * cartItem.getQuantity());

        // 200
        cartItem.setProductPrice(product.getSpecialPrice());

        // 800 + 200*2 = 1200
        cart.setTotalPrice(cartPrice + (cartItem.getProductPrice() * cartItem.getQuantity()));

        cartItem = cartItemRepository.save(cartItem);

    }
}
