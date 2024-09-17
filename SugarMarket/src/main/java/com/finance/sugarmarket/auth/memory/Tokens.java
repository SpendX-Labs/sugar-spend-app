package com.finance.sugarmarket.auth.memory;
import java.util.HashMap;
import java.util.Map;

import com.finance.sugarmarket.auth.model.Token;

public class Tokens {
	public static Map<String, Token> tokenMap = new HashMap<>();
}