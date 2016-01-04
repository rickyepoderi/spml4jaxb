/* 
 * Copyright (c) 2015 ricky <https://github.com/rickyepoderi/spml4jaxb>
 * 
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *  See the file COPYING included with this distribution for more
 *  information.
 */
package es.rickyepoderi.spml4jaxb.test;

import es.rickyepoderi.spml4jaxb.accessor.FilterAccessor;
import es.rickyepoderi.spml4jaxb.accessor.RequestAccessor;
import es.rickyepoderi.spml4jaxb.accessor.SearchQueryAccessor;
import es.rickyepoderi.spml4jaxb.accessor.SearchRequestAccessor;
import es.rickyepoderi.spml4jaxb.builder.LookupResponseBuilder;
import es.rickyepoderi.spml4jaxb.builder.ResponseBuilder;
import es.rickyepoderi.spml4jaxb.builder.SearchResponseBuilder;
import es.rickyepoderi.spml4jaxb.msg.core.PSOType;
import es.rickyepoderi.spml4jaxb.msg.dsmlv2.AttributeDescription;
import es.rickyepoderi.spml4jaxb.msg.dsmlv2.AttributeValueAssertion;
import es.rickyepoderi.spml4jaxb.msg.dsmlv2.DsmlAttr;
import es.rickyepoderi.spml4jaxb.user.ManagerException;
import es.rickyepoderi.spml4jaxb.user.User;
import es.rickyepoderi.spml4jaxb.user.UserManager;
import javax.xml.bind.JAXBContext;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import javax.xml.xpath.XPathExpressionException;

/**
 *
 * @author ricky
 */
public class SearchExecutor extends AsyncSpmlBaseExecutor {

    private UserManager um;
    private int iteratorSize = -1;
    protected JAXBContext ctx = null;
    
    private static Map<String, SearchWrapper> searches = new ConcurrentHashMap<>();
    
    protected class SearchWrapper {
        PSOType[] psos;
        int number;
    }
    
    static protected SearchWrapper getSearch(String id) {
        return searches.get(id);
    }
    
    static protected boolean removeSearch(String id) {
        return searches.remove(id) != null;
    }
            
    static protected Map<String, DsmlAttr> filterAttributes(Map<String, DsmlAttr> attrs, 
            Set<String> returnAttrsSet) {
        if (returnAttrsSet == null) {
            return attrs;
        }
        Map<String, DsmlAttr> res = new HashMap<>();
        for (String name: attrs.keySet()) {
            if (returnAttrsSet.contains(name)) {
                res.put(name, attrs.get(name));
            }
        }
        return res;
    }
    
    protected static boolean matchEquals(Map<String, DsmlAttr> attrs, AttributeValueAssertion filter) {
        String name = filter.getName();
        DsmlAttr attr = attrs.get(name);
        if (attr == null) {
            return false;
        } else if (filter.getValue() == null) {
            return false;
        } else {
            for (String v: attr.getValue()) {
                boolean res = filter.getValue().equals(v);
                if (res) {
                    return true;
                }
            }
            return false;
        }
    }
    
    protected static boolean matchGreaterOrEqual(Map<String, DsmlAttr> attrs, AttributeValueAssertion filter) {
        String name = filter.getName();
        DsmlAttr attr = attrs.get(name);
        if (attr == null) {
            return false;
        } else if (filter.getValue() == null) {
            return false;
        } else {
            for (String v: attr.getValue()) {
                boolean res = filter.getValue().compareTo(v) <= 0;
                if (res) {
                    return true;
                }
            }
            return false;
        }
    }
    
    protected static boolean matchLessOrEqual(Map<String, DsmlAttr> attrs, AttributeValueAssertion filter) {
        String name = filter.getName();
        DsmlAttr attr = attrs.get(name);
        if (attr == null) {
            return false;
        } else if (filter.getValue() == null) {
            return false;
        } else {
            for (String v: attr.getValue()) {
                boolean res = filter.getValue().compareTo(v) >= 0;
                if (res) {
                    return true;
                }
            }
            return false;
        }
    }
    
    protected static boolean matchStartsWith(Map<String, DsmlAttr> attrs, AttributeValueAssertion filter) {
        String name = filter.getName();
        DsmlAttr attr = attrs.get(name);
        if (attr == null) {
            return false;
        } else if (filter.getValue() == null) {
            return false;
        } else {
            for (String v: attr.getValue()) {
                boolean res = v.startsWith(filter.getValue());
                if (res) {
                    return true;
                }
            }
            return false;
        }
    }
    
    protected static boolean matchEndsWith(Map<String, DsmlAttr> attrs, AttributeValueAssertion filter) {
        String name = filter.getName();
        DsmlAttr attr = attrs.get(name);
        if (attr == null) {
            return false;
        } else if (filter.getValue() == null) {
            return false;
        } else {
            for (String v: attr.getValue()) {
                boolean res = v.endsWith(filter.getValue());
                if (res) {
                    return true;
                }
            }
            return false;
        }
    }
    
    protected static boolean matchContains(Map<String, DsmlAttr> attrs, AttributeValueAssertion filter) {
        String name = filter.getName();
        DsmlAttr attr = attrs.get(name);
        if (attr == null) {
            return false;
        } else if (filter.getValue() == null) {
            return false;
        } else {
            for (String v : attr.getValue()) {
                boolean res = v.contains(filter.getValue());
                if (res) {
                    return true;
                }
            }
            return false;
        }
    }
    
    protected static boolean matchApprox(Map<String, DsmlAttr> attrs, AttributeValueAssertion filter) {
        return matchEquals(attrs, filter);
    }
    
    protected static boolean matchPresent(Map<String, DsmlAttr> attrs, AttributeDescription filter) {
        String name = filter.getName();
        DsmlAttr attr = attrs.get(name);
        if (attr == null) {
            return false;
        } else {
            return !attr.getValue().isEmpty();
        }
    }
    
    protected static String mapSQLName(String name) throws ManagerException {
        if (null != name) {
            switch (name) {
                case "uid":
                    return "u.USERNAME";
                case "cn":
                    return "COMMON_NAME";
                case "description":
                    return "DESCRIPTION";
                case "role":
                    return "ROLENAME";
                default:
                    throw new ManagerException("Unknown name in filter: " + name);
            }
        } else {
            throw new ManagerException("Cannot map a null name");
        }
    }
    
    protected static String xpath2SQL(String xpath) throws ManagerException, XPathExpressionException {
        // check is a valid XPath (exception is thrown)
        if (xpath == null || xpath.isEmpty()) {
            return null;
        } if (xpath.matches("//user|//usr:user|/user|/usr:user")) {
            // return all users
            return null;
        } else if (xpath.matches("(//user|//usr:user|/user|/usr:user)\\[.+\\]")) {
            // check xpath is correct
            XPath xp = XPathFactory.newInstance().newXPath();
            xp.compile(xpath);
            // just manage
            String sql = xpath.substring(xpath.indexOf("[") + 1);
            sql = sql.substring(0, sql.length() - 1);
            // replace every name with the SQL name => very simple converter
            return sql.replace("usr:user", "u.USERNAME")
                    .replace("usr:cn", "COMMON_NAME")
                    .replace("usr:description", "DESCRIPTION")
                    .replace("usr:role", "ROLENAME")
                    .replace("usr:uid", "u.USERNAME")
                    .replace("uid", "u.USERNAME")
                    .replace("cn", "COMMON_NAME")
                    .replace("description", "DESCRIPTION")
                    .replace("role", "ROLENAME")
                    .replace("uid", "u.USERNAME")
                    .replace("\"", "'");
        } else {
            throw new ManagerException("Cannot convert XPATH to SQL filter.");
        }
    }
    
    protected static void filter2SQL(FilterAccessor filter, StringBuilder sb) throws ManagerException {
        if (filter != null) {
            if (filter.isEquals()) {
                AttributeValueAssertion ava = filter.getEquals();
                sb.append("(").append(mapSQLName(ava.getName())).append(" = '").append(ava.getValue()).append("')");
            } else if (filter.isGreaterOrEqual()) {
                AttributeValueAssertion ava = filter.getGreaterOrEqual();
                sb.append("(").append(mapSQLName(ava.getName())).append(" >= '").append(ava.getValue()).append("')");
            } else if (filter.isLessOrEqual()) {
                AttributeValueAssertion ava = filter.getLessOrEqual();
                sb.append("(").append(mapSQLName(ava.getName())).append(" <= '").append(ava.getValue()).append("')");
            } else if (filter.isPresent()) {
                AttributeDescription ava = filter.getPresent();
                sb.append("(").append(mapSQLName(ava.getName())).append(" is not NULL)");
            } else if (filter.isApproxMatch()) {
                AttributeValueAssertion ava = filter.getApproxMatch();
                sb.append("(").append(mapSQLName(ava.getName())).append(" = '").append(ava.getValue()).append("')");
            } else if (filter.isStartsWith()) {
                AttributeValueAssertion ava = filter.getStartsWith();
                sb.append("(").append(mapSQLName(ava.getName())).append(" LIKE '").append(ava.getValue()).append("%')");
            } else if (filter.isEndsWith()) {
                AttributeValueAssertion ava = filter.getEndsWith();
                sb.append("(").append(mapSQLName(ava.getName())).append(" LIKE '%").append(ava.getValue()).append("')");
            } else if (filter.isContains()) {
                AttributeValueAssertion ava = filter.getContains();
                sb.append("(").append(mapSQLName(ava.getName())).append(" LIKE '%").append(ava.getValue()).append("%')");
            } else if (filter.isNot()) {
                sb.append(" not ");
                filter2SQL(filter.getNot(), sb);
            } else if (filter.isAnd()) {
                sb.append("(");
                for (FilterAccessor f : filter.getAnd()) {
                    filter2SQL(f, sb);
                    sb.append(" and ");
                }
                sb.setLength(sb.length() - 5);
                sb.append(")");
            } else if (filter.isOr()) {
                sb.append("(");
                for (FilterAccessor f : filter.getOr()) {
                    filter2SQL(f, sb);
                    sb.append(" or ");
                }
                sb.setLength(sb.length() - 4);
                sb.append(")");
            } else {
                throw new ManagerException("Cannot map to SQL filter.");
            }
        }
    }
    
    protected static String filter2SQL(FilterAccessor filter) throws ManagerException {
        StringBuilder sb = new StringBuilder();
        filter2SQL(filter, sb);
        return sb.toString();
    }
    
    protected static boolean matchFilter(Map<String, DsmlAttr> attrs, FilterAccessor filter, SearchRequestAccessor req) {
        if (filter == null) {
            // return all
            return true;
        }
        if (filter.isEquals()) {
            return matchEquals(attrs, filter.getEquals());
        } else if (filter.isGreaterOrEqual()) {
            return matchGreaterOrEqual(attrs, filter.getGreaterOrEqual());
        } else if (filter.isLessOrEqual()) {
            return matchLessOrEqual(attrs, filter.getLessOrEqual());
        } else if (filter.isPresent()) {
            return matchPresent(attrs, filter.getPresent());
        } else if (filter.isApproxMatch()) {
            return matchApprox(attrs, filter.getApproxMatch());
        } else if (filter.isStartsWith()) {
            return matchStartsWith(attrs, filter.getStartsWith());
        } else if (filter.isEndsWith()) {
            return matchEndsWith(attrs, filter.getEndsWith());
        } else if (filter.isContains()) {
            return matchContains(attrs, filter.getContains());
        } else if (filter.isNot()) {
            return !matchFilter(attrs, filter.getNot(), req);
        } else if (filter.isAnd()) {
            for (FilterAccessor f: filter.getAnd()) {
                if (!matchFilter(attrs, f, req)) {
                    return false;
                }
            }
            return true;
        } else if (filter.isOr()) {
            for (FilterAccessor f: filter.getOr()) {
                if (matchFilter(attrs, f, req)) {
                    return true;
                }
            }
            return false;
        } else {
            return false;
        }
    }
    
    public SearchExecutor(UserManager um, WorkQueue queue, JAXBContext ctx, int iteratorSize) {
        super(queue);
        this.um = um;
        this.iteratorSize = iteratorSize;
        this.ctx = ctx;
    }

    @Override
    public RequestAccessor specificAccessor(RequestAccessor request) {
        return request.asSearch();
    }

    @Override
    public ResponseBuilder realExecute(WorkQueue.WorkItem item) {
        SearchResponseBuilder builder = ResponseBuilder.builderForSearch().requestId(item.getId());
        SearchRequestAccessor req = item.getRequestAccessor().asSearch();
        try {
            SearchQueryAccessor query = req.getQuery();
            if (query != null) {
                //List<User> users = um.list(null);
                if (query.isTargetId(ListTargetsExecutor.DSML_TARGET_ID)) {
                    FilterAccessor filter = query.getQueryFilter();
                    String[] returnAttrs = query.getDsmlAttributes();
                    Set<String> returnAttrsSet = new HashSet<>();
                    if (returnAttrs != null) {
                        returnAttrsSet = new HashSet(Arrays.asList(query.getDsmlAttributes()));
                    }
                    // search using the filter
                    List<User> users = um.list(filter2SQL(filter));
                    // create the wrapper for the response
                    List<PSOType> result = new ArrayList<>();
                    int i = 0;
                    String iterId = null;
                    for (User user: users) {
                        Map<String, DsmlAttr> attrs = AddExecutor.user2DsmlMap(user);
                        //if (matchFilter(attrs, filter, req)) {
                            attrs = filterAttributes(attrs, returnAttrsSet);
                            // create another response to get the PSO
                            LookupResponseBuilder lookup = ResponseBuilder.builderForLookup()
                                    .psoId(user.getUid()).psoTargetId(query.getTargetId());
                            if (!attrs.isEmpty() && (req.isReturnData() || req.isReturnEverything())) {
                                lookup.dsmlAttribute(attrs.values().toArray(new DsmlAttr[0]));
                            }
                            PSOType pso = lookup.asAccessor().asLookup().getPso();
                            i++;
                            if (i <= iteratorSize) {
                                // add to the current response
                                builder.pso(pso).nextPso();
                            } else if (i == iteratorSize + 1) {
                                // the search should be added to the map and iterator returned
                                iterId = UUID.randomUUID().toString();
                                builder.iteratorId(iterId);
                                result.add(pso);
                            } else {
                                // just add to the iterator search saved
                                result.add(pso);
                            }
                        //}
                    }
                    if (iterId != null) {
                        // add to the searched
                        SearchWrapper wrap = new SearchWrapper();
                        wrap.psos = result.toArray(new PSOType[0]);
                        wrap.number = 0;
                        searches.put(iterId, wrap);
                    }
                    builder.success();
                } else if (query.isTargetId(ListTargetsExecutor.XSD_TARGET_ID)) {
                    // get the XPath
                    String xpathString = query.getXsdXPathSelection();
                    if (xpathString != null) {
                        //XPath xpath = XPathFactory.newInstance().newXPath();
                        //xpath.setNamespaceContext(new NamespaceContext() {
                        //    @Override
                        //    public String getNamespaceURI(String prefix) {
                        //        return prefix.equals("usr") ? "urn:ddbb-spml-dsml:user" : null;
                        //    }

                        //    @Override
                        //    public Iterator getPrefixes(String val) {
                        //        return null;
                        //    }

                        //    @Override
                        //    public String getPrefix(String uri) {
                        //        return uri.equals("urn:ddbb-spml-dsml:user") ? "usr" : null;
                        //    }
                        //});
                        List<User> users = um.list(xpath2SQL(xpathString));
                        List<PSOType> result = new ArrayList<>();
                        int i = 0;
                        String iterId = null;
                        for (User user: users) {
                            //Document doc = ModifyExecutor.user2Doc(user, ctx);
                            //boolean ok = (Boolean) xpath.evaluate(xpathString, doc.getDocumentElement(), XPathConstants.BOOLEAN);
                            //if (!ok) {
                            //    // try without namespaces
                            //    ModifyExecutor.changeNamespaces(doc, doc.getDocumentElement(), "");
                            //    ok = (Boolean) xpath.evaluate(xpathString, doc.getDocumentElement(), XPathConstants.BOOLEAN);
                            //}
                            //if (ok) {
                                LookupResponseBuilder lookup = ResponseBuilder.builderForLookup()
                                        .psoId(user.getUid()).psoTargetId(query.getTargetId());
                                if (req.isReturnData() || req.isReturnEverything()) {
                                    lookup.xsdObject(user);
                                }
                                PSOType pso = lookup.asAccessor().getPso();
                                i++;
                                if (i <= iteratorSize) {
                                    // add to the current response
                                    builder.pso(pso).nextPso();
                                } else if (i == iteratorSize + 1) {
                                    // the search should be added to the map and iterator returned
                                    iterId = UUID.randomUUID().toString();
                                    builder.iteratorId(iterId);
                                    result.add(pso);
                                } else {
                                    // just add to the iterator search saved
                                    result.add(pso);
                                }
                            //}
                        }
                        if (iterId != null) {
                            // add to the searched
                            SearchWrapper wrap = new SearchWrapper();
                            wrap.psos = result.toArray(new PSOType[0]);
                            wrap.number = 0;
                            searches.put(iterId, wrap);
                        }
                        builder.success();
                    } else {
                        builder.failure().malformedRequest().errorMessage("The XPath is compulsoty in a XSD searxh.");
                    }
                } else {
                    builder.failure().invalidIdentifier().errorMessage("Invalid search target id");
                }
            } else {
                builder.failure().malformedRequest()
                        .errorMessage("There are two targets, a search element is needed.");
            }
        } catch (ManagerException|XPathExpressionException e) {
            builder.failure().customError().errorMessage(e.getMessage());
        }
        queue.finish(item, builder);
        return builder;
    }
    
}
