import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.inject.New;

import org.junit.Test;


public class ListInMapTest {

	@Test
	public void test() {
		Map<Long, List<String>> testMap = new HashMap<Long, List<String>>();
		List<String> testList1 = new ArrayList<String>();
		testList1.add("test1");
		testList1.add("test2");
		List<String> testList2 = new ArrayList<String>();
		testList2.add("test3");
		testList2.add("test4");
		testMap.put(new Long(1), testList1);
		testMap.put(new Long(2), testList2);
		System.out.println(testMap);
		System.out.println(testMap.get(new Long(4)) == null);
		//testMap.get(new Long(3)).add("test5");
		testMap.get(new Long(2)).remove("test3");
		System.out.println(testMap);
	}

}
