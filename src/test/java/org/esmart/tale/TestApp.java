package org.esmart.tale;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.esmart.tale.jetag.Commons;
import org.esmart.tale.mapper.TcontentsMapper;
import org.esmart.tale.mapper.ToptionsMapper;
import org.esmart.tale.model.Toptions;
import org.esmart.tale.utils.ApplicationContextUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-context.xml")
public class TestApp {

	@Resource
	private ToptionsMapper optionsMapper;
	@Resource
	private TcontentsMapper contentsMapper;
	@Resource
	private SqlSessionFactory sqlSessionFactory;
//	@Test
	public void test(){
		//分页处理，
        PageHelper.startPage(2, 3);
		List<Toptions> ops = optionsMapper.selectAll();
		
		PageInfo<Toptions> page = new PageInfo<Toptions>(ops);
		System.out.println("EndRow:"+page.getEndRow());
		System.out.println("FirstPage:"+page.getFirstPage());
		System.out.println("LastPage:"+page.getLastPage());
		System.out.println("NavigatePages:"+page.getNavigatePages());
		System.out.println("NextPage:"+page.getNextPage());
		System.out.println("OrderBy:"+page.getOrderBy());
		System.out.println("PageNum:"+page.getPageNum());
		System.out.println("Pages:"+page.getPages());
		System.out.println("PageSize:"+page.getPageSize());
		System.out.println("PrePage:"+page.getPrePage());
		System.out.println("Total:"+page.getTotal());
//		System.out.println(page.getList());
		page.getList().forEach(e ->System.out.println("打印："+e.getName()));
		
		System.out.println("\n=================================\n");
		List<HashMap<String,Object>> map = contentsMapper.selectMap();
		System.out.println(map.toString());
		
		System.out.println(sqlSessionFactory.openSession().getConnection().toString());;
	}
	
	@Test
	public void testdate(){
		
		System.out.println(Commons.fmtdate(1510889156, "yyyy-MM-dd HH:mm:ss"));
		
	}
}
