package com.test.javaPractice;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class _1 {

	

	public static void main(String[] args) {
		
//		Given a list of integers, find out all the even numbers that exist in the list using Stream functions?
//		10,15,8,49,25,98,32
		List<Integer> li = Arrays.asList(1,15,8,49,25,98,32,15,10,49,55,60,65);
		
//		with using norman approach
//		for(Integer i : li) {
//			if(i%2==0) {
//				System.out.println(i);
//			}
//		}
		
//		with using java 8
//		li.stream().filter(x->x%2==0).forEachOrdered(System.out::println);
		
//		Given a list of integers, find out all the numbers starting with 1 using Stream functions?
		
//		for(Integer i : li) {
//			String numberString = Integer.toString(i);
//			
//			if(numberString.startsWith("1")) {
//				System.out.println(i);
//			}
//		}
		
		
//		li.stream().map(s->s+"").filter(s->s.startsWith("1")).forEach(System.out::println);
		
//		 How to find duplicate elements in a given integers list in java using Stream functions?
		HashSet<Integer> set = new HashSet<>();
        // List to store duplicates
        List<Integer> duplicates = new ArrayList<>();

        // Iterating through the list to find duplicates
        for (Integer i : li) {
            // If the set already contains the element, it's a duplicate
            if (!set.add(i)) {
                duplicates.add(i);
            }
        }

        // Output the duplicates
        System.out.println("Duplicates: " + duplicates);
		
//		li.stream().distinct().forEach(System.out::println);
        
//        Given the list of integers, find the first element of the list using Stream functions?
//        li.stream().findFirst().ifPresent(System.out::println);
        
//        Given a list of integers, find the total number of elements present in the list using Stream functions?
        long count = li.stream().count();
//        System.out.println(count);
        
//        Given a list of integers, find the maximum value element present in it using Stream functions?
        Integer integer = li.stream().max(Integer::compare).get();
//        System.out.println(integer);
        
//        Given a list of integers, sort all the values present in it using Stream functions?
        li.stream().sorted((a,b)->b-a).forEach(System.out::println);
        
//        or
        li.stream().sorted(Collections.reverseOrder()).forEachOrdered(System.out::println);
		
        
//        How will you get the current date and time using Java 8 Date and Time API?
        System.out.println("The local date is  : "+LocalDate.now());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("mm:HH:ss");
        System.out.println("Local time is : " +LocalTime.now().format(formatter));
        
//        Write a Java 8 program to concatenate two Streams?
        List<String> list2 = Arrays.asList("explained", "through", "programs");
        
        Stream.concat(li.stream(), list2.stream()).forEach(System.out::print);;
        
        
//         Java 8 program to perform cube on list elements and filter numbers greater than 50.
        li.stream().map(i->i*i*i).filter(x->x>50).forEachOrdered(System.out::println);
        
//        How to use map to convert object into Uppercase in Java 8?
        list2.stream().map(String::toUpperCase).forEach(System.out::println);
        
        
//       How to count each element/word from the String ArrayList in Java8?
        
        
        for(String s : list2) {
        	char[] charArray = s.toCharArray();
        	for(int i=charArray[0]; i>=charArray.length; i++) {
        		System.out.println("the length of first character is : " +i);
        	}
        }
        
        
	}
	
	
}
