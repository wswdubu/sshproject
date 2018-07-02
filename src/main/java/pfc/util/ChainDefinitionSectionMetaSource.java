package pfc.util;

import java.text.MessageFormat;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.config.Ini;
import org.apache.shiro.config.Ini.Section;
import org.springframework.beans.factory.FactoryBean;

import pfc.bean.Privilege;
import pfc.service.privilege.PrivilegeService;

public class ChainDefinitionSectionMetaSource implements FactoryBean<Ini.Section> {

	private PrivilegeService priService;
	private String filterChainDefinitions;
	/**
     * 默认premission字符串
     */
    public static final String PREMISSION_STRING="perms[\"{0}\"]";

	public Section getObject() throws Exception {
		List<Privilege> plist=priService.getAll();
		Ini ini=new Ini();
		ini.load(filterChainDefinitions);
		Ini.Section section =ini.getSection(Ini.DEFAULT_SECTION_NAME);
		//循环Privilege的url,逐个添加到section中。section就是filterChainDefinitionMap,
        //里面的键就是链接URL,值就是存在什么条件才能访问该链接
        for (Iterator<Privilege> it = plist.iterator(); it.hasNext();) {

        	Privilege privilege = it.next();
            //如果不为空值添加到section中
            if(StringUtils.isNotEmpty(privilege.getPriurl()) && StringUtils.isNotEmpty(privilege.getPriname())) {
                section.put(privilege.getPriurl(),  MessageFormat.format(PREMISSION_STRING,privilege.getPriname()));
            }
        }
        for(String ss:section.keySet()){
        	System.out.println(ss+"----------"+section.get(ss));
        }
        return section;
	}
	
	public Class<?> getObjectType() {
		return this.getClass();
	}

	public boolean isSingleton() {
		return false;
	}



	public String getFilterChainDefinitions() {
		return filterChainDefinitions;
	}

	public void setFilterChainDefinitions(String filterChainDefinitions) {
		this.filterChainDefinitions = filterChainDefinitions;
	}

	public PrivilegeService getPriService() {
		return priService;
	}

	public void setPriService(PrivilegeService priService) {
		this.priService = priService;
	}
	
	
	

}
