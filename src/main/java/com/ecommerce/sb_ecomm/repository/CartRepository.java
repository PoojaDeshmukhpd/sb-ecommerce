package com.ecommerce.sb_ecomm.repository;

import com.ecommerce.sb_ecomm.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    /* i am having cart and in cart within user i am having email
     JPA will not genertae query for nested objects */
    @Query("SELECT c FROM Cart c WHERE c.user.email = ?1")
    Cart findCartByEmail(String email);

    // "No Property Name Email" if i not write down below query then it will give this error
    @Query("SELECT c FROM Cart c WHERE c.user.email =?1 AND c.id = ?2")
    Cart findCartByEmailAndCartId(String emailId, Long cartId);

    @Query("SELECT c FROM Cart c WHERE c JOIN FETCH c.cartItems ci JOIN FETCH ci.product p WHERE p.id = ?1")
    List<Cart> findCartsByProductId(Long productId);
}
