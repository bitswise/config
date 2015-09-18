package com.bitswise.tinyurl;

public class ShortURLTest {

	public void testResolveFullURL() {
		for (long i = 0; i < 1000000; i++) {
			String longUrl = "http://bitswise.com/" +  i;
			String shortUrl = ShortURL.shorten(longUrl);
			String longUrlFromShortUrl = ShortURL.toUrl(shortUrl);
			if (i % 100000 == 0) {
				System.out.println("Short Output: Long URL: " + longUrl + "; shortUrl: " + shortUrl);
				System.out.println("toUrl Output: Short URL: " + shortUrl  + "; longUrl: " + longUrlFromShortUrl);
			}
		}	
	}
	
	public static void main(String[] args) {
		//new ShortURLTest().testShortURL();
		new ShortURLTest().testResolveFullURL();
	}
}
