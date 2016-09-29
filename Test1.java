import com.oo.vul.domain.Rule;


public class Test1 {

	public static void main(String[] args) {
		Rule rule=new Rule();
		rule.setRuleDesc("影响Redhat4/5");
		System.out.println(Test1.onBeforeMatchRule(rule, "redhat6.1"));
	}
    public static boolean onBeforeMatchRule(Rule rule,String series){
    	if(!rule.getRuleDesc().startsWith("影响")||series==null||"".equals(series)){
    		return false;
    	}
    	String ruleDesc=rule.getRuleDesc().replaceAll("[^RrEeDdHhAaTt0-9/]", "");
    	if("".equals(ruleDesc)||ruleDesc.length()<7){
    		return false;
    	}
    	//REDHAT4/5/6/7
    	String strs[]=ruleDesc.replaceAll("[^0-9/]","").split("\\/");
    	for(String str:strs){
    		 series=series.trim().toUpperCase();
    		if(series.indexOf("REDHAT"+str)!=-1){
    			return false;
    		}
    	}
    	return true;
    }
}
