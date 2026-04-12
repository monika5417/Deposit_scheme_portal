package com.mpcz.deposit_scheme.projection;

import org.aspectj.weaver.ast.Test;

public class TestData {
	
//	Reverse String
	public static void reverseString(String str) {
		String reverseStr = "";
		for(int i=str.length()-1; i>=0; i--) {
			reverseStr +=str.charAt(i);
		}
		
		System.err.println("Original String : " +str);
		System.err.println("Reverse String : " +reverseStr);
	}
	
	
//	Pallindrom String
	public static void pallindromeString(String str) {
		String revStr = "";
		for(int i=str.toLowerCase().length()-1; i>=0;i--) {
			revStr +=str.charAt(i);
		}
		
		if(str.toLowerCase().equals(revStr.toLowerCase())) {
			System.err.println("The given string is pallindrom : " +str.toLowerCase());
		}else {
			System.err.println("The given string is not pallindrom : " +str.toLowerCase());
		}
	}
	
	
//	Fibonacci Series
	public static void fibonacciSeries(int n) {
		
		int a = 0, b=1;
		
		for(int i=0; i<n;i++) {
			   System.out.print(a + " ");

			int t=a+b; a=b; b=t;
		}
		
	}
	
//	PRIME NO. CHECK
	public static void checkPrimeOrNot(int n) {
		if(n<2) 
			System.err.println(n+ " is less then 2");
		else if(n%2==0) 
			System.err.println(n+ " is not prime  no.");
		else 
			System.err.println(n+ " is prime no.");
	}
	
//	swapping programme
	public static void swapNo(int a, int b) {
		System.err.println("Without swaping value a : " +a + " b : " +b);
		int temp =a;
		a=b;
		b=temp;
		System.err.println(" swaping value a : " +a + " b : " +b);
		
	}
	
//	swapping without 3rd variable
	public static void swapWithout3rdVariable(int a , int b) {
		System.err.println("swapping without 3rd variable");
		System.err.println("Without swaping value a : " +a + " b : " +b);
	      a = a+b;
	      b=a-b;
	      
	      a= a-b;
	      
	      
	      System.err.println(" swaping value a : " +a + " b : " +b);
	
	}
	
	
	public static int  factorialMethod(int n) {
		   if(n == 0) return 1;
		    return n * factorialMethod(n-1);

	}
	
	
	public static void main(String[] args) {
		String str = "Madam";
		
//		TestData.reverseString(str);
//		TestData.pallindromeString(str);
//		TestData.fibonacciSeries(9);
//		TestData.checkPrimeOrNot(7);
//		TestData.swapNo(10, 20);
//		TestData.swapWithout3rdVariable(20, 30);
		System.err.println(TestData.factorialMethod(1) );;
		
	}
	

}
