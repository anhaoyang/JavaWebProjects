package com.anhaoyang.demo.ssm;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.anhaoyang.demo.ssm.cache.ehcache.EhCacheCacheManagerFactory;


@RunWith(SpringRunner.class)
@SpringBootTest
public class SsmDemoApplicationTests {
	
	@Autowired
	private EhCacheCacheManagerFactory ehCacheCacheManagerFactory;
	
	@Test
	public void TestMapper() {
		System.out.println(ehCacheCacheManagerFactory);
	}
}
