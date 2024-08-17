package com.test.javaPractice;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Test {

	
	public static void main(String[] args) {
		// Example list: List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
		// Output: 1, 3, 5, 7, 9
		List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
		numbers.stream().filter(i->i%2!=0).forEach(System.out::println);
		List<Integer> collect = numbers.stream().filter(i->i%2!=0).collect(Collectors.toList());
		System.out.println(collect);
		
	}
	
	
	
}
