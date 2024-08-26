package com.hkit.lessons.cart;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hkit.lessons.DataNotFoundException;
import com.hkit.lessons.lesson.Lesson;
import com.hkit.lessons.member.Member;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartService {

    @Autowired
    private final CartRepository cartRepository;

    public List<Cart> getCart(){
    	return cartRepository.findAll();
    }
    
    
    public List<Cart> getCartByMember(Member member) {
        return cartRepository.findByMember(member);
    }

    public Cart GetCart(Long listNum) {
		Optional<Cart> cart = this.cartRepository.findByListNum(listNum);
		
		return cart.get();
	}
    
    public void addToCart(Lesson lessonNum, Member memberNum,char opt) {
    	Cart cart = new Cart();

    	cart.setMember(memberNum);
    	cart.setLesson(lessonNum);
    	cart.setCreateDate(LocalDateTime.now());
    	cart.setOpt(opt);
    	
        cartRepository.save(cart);
    }

    public void updateCartItem(int ListNum, char opt) {
        Cart item = cartRepository.findByListNum(ListNum);
        item.setOpt(opt);
        cartRepository.save(item);
    }

	public void delete(Cart cart) {
		this.cartRepository.delete(cart);	
	}
}