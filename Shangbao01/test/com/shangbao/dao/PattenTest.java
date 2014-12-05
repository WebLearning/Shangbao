package com.shangbao.dao;

import static org.junit.Assert.*;

import org.junit.Test;

public class PattenTest {

	@Test
	public void test() {
		System.out.println("4".matches("[\\d]+(?:_[\\d]+)*"));
	}

}
