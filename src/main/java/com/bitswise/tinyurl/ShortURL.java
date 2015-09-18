package com.bitswise.tinyurl;
import java.math.BigInteger;
import java.net.URL;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;


public final class ShortURL {
	
	// 62 characters
	final static String ALPHABET = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHUJKLMNOPQRSTUVWXYZ"; 
	static AtomicLong counter = new AtomicLong(-1);
	static ConcurrentMap<String, Long> longUrlToIndex = new ConcurrentHashMap<String, Long>();
	static ConcurrentMap<Long, String> indexToLongUrl = new ConcurrentHashMap<Long, String>();
	
	public static String shorten(String url) {
		Long index = longUrlToIndex.get(url);
		if (index == null) {
			index = counter.incrementAndGet();
		}
		longUrlToIndex.putIfAbsent(url, index);
		indexToLongUrl.put(index, url);
		return toBase62String(index);
	}
	
	
	public static String toUrl(String shortUrl) {
		StringBuilder result = new StringBuilder();
		long index = 0;
		BigInteger base = BigInteger.valueOf(62);
		for (int i = shortUrl.length(); i > 0 ; i--) {
			index = index + ALPHABET.indexOf(shortUrl.charAt(i - 1)) * base.pow(shortUrl.length() - i).longValue();
		}
		return indexToLongUrl.get(index);
	}
	
	public static String toBase62String(Long index) {
		StringBuilder result = new StringBuilder();
		do {
			result.append(ALPHABET.charAt((int)(index % 62)));
			index = index / 62;
		} while (index > 0);
		return result.reverse().toString();
	}
}
