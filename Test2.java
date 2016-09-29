import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import com.oo.vul.util.DomainCache;


public class Test2 {

	public static void main(String[] args)throws Exception {
		// XML是树形结构 所有节点是树形结构的
		// new SAXBuilder 创建 一个解析器
		// 解析器去解析生成一个文档Document对象 
		// 然后拿着文档对象先得到根节点 doc.getRootElement()
		// 然后由根节点去获取所有子节点 主要常用到的方法有 1：获取指定子节点root.getChild("子节点名称")返回节点，2：获取所有子节点返回list,root.getChildren()
		// 由上一步得到的节点Element取值，有两种
		//  1：e.getAttributeValue("name") 解析类似这种的值(解析节点内属性的值)<node name="2234"></node>
		//  2:e.getTextTrim()解析类似这种的值(解析标签之间的值)<node>2432342</node>
		    SAXBuilder builder=new SAXBuilder();//
		    File file=new File("/..");
			Document doc = builder.build(DomainCache.class.getResourceAsStream("/conf.xml"));
			Element root = doc.getRootElement();
			List<Element> els = root.getChild("domains").getChildren();
			for(Element e:els){
				String name = e.getAttributeValue("name");
				List<Element> children = e.getChildren();
				List<String> list=new ArrayList<String>();
				for(Element ee:children){
					list.add(ee.getTextTrim());
				}
			}
	}
}
