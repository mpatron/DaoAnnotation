/*
 * Created on 6 juil. 2003
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
public class WhereFields {

  private final class WhereField {
    private WhereField (){};
    //String fieldname;
    private ComparatorEnum comparator;
    private String whereAssertion;
  }

  private Map<String, WhereField> hm= new HashMap<String, WhereField>();
  private List<String> v= new ArrayList<String>();

  public final void addColumn(String fieldname, ComparatorEnum comparator, String whereAssertion) {
    WhereField wf = new WhereField();
    //wf.fieldname=fieldname;
    wf.comparator=comparator;
    wf.whereAssertion=whereAssertion;
    hm.put(fieldname, wf);
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

  public final ComparatorEnum getComparator(int i) {
    ComparatorEnum returnValue=null;
    String fieldname=getFieldName(i);
    WhereField wf = (WhereField)hm.get(fieldname);
    returnValue=wf.comparator;
    return returnValue;
  }
  
  public final String getWhereAssertion(int i) {
    String returnValue=null;
    String fieldname=getFieldName(i);
    WhereField wf = (WhereField)hm.get(fieldname);
    returnValue=wf.whereAssertion;
    return returnValue;
  }
  
  public final Map<String, WhereField> getList() {
    return hm;
  }
}
