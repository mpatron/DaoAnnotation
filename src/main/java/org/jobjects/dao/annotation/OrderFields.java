/*
 * Created on 21 juin 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package org.jobjects.dao.annotation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ComparatorEnum
 * @author Mickaël Patron
 * @since 1.0.0
 */
public class OrderFields {

  private Map<String, Boolean> hm= new HashMap<String, Boolean>();
  private List<String> v= new ArrayList<String>();

  public final void addColumn(String fieldname, boolean ascending) {
    hm.put(fieldname, ascending);
    v.add(fieldname);
  }

  public final void delColumn(String fieldname) {
    hm.remove(fieldname);
    v.remove(fieldname);
  }
  
  public final int size() {
    return hm.size();
  }

  public final String getFieldName(int i) {
    String returnValue=null;
    returnValue=(String)v.get(i);    
    return returnValue;
  }
  
  public final boolean getAscending(int i) {
    boolean returnValue=false;
    String fieldname=getFieldName(i);
    returnValue=((Boolean)hm.get(fieldname)).booleanValue();
    return returnValue;
  }
  
  public final Map<String, Boolean> getList() {
    return hm;
  }
}
