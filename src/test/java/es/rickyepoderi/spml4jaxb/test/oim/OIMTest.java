/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.rickyepoderi.spml4jaxb.test.oim;

import es.rickyepoderi.spml4jaxb.accessor.AddResponseAccessor;
import es.rickyepoderi.spml4jaxb.accessor.ListTargetsResponseAccessor;
import es.rickyepoderi.spml4jaxb.accessor.LookupResponseAccessor;
import es.rickyepoderi.spml4jaxb.builder.RequestBuilder;
import es.rickyepoderi.spml4jaxb.client.SpmlException;
import es.rickyepoderi.spml4jaxb.client.WSSUsernamePasswordClient;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 *
 * @author ricky
 */
public class OIMTest {
    
    private ObjectFactory psoObjectFactory = new ObjectFactory();
    
    @Test
    public void testAsync() throws SpmlException {
        WSSUsernamePasswordClient client = new WSSUsernamePasswordClient(
                "http://10.0.2.12:14000/spml-xsd/SPMLService", "xelsysadm", "Kiosko_00",
                true, es.rickyepoderi.spml4jaxb.test.oim.ObjectFactory.class);
        // listTargets
        ListTargetsResponseAccessor ltra = RequestBuilder.builderForListTargets()
                .synchronous()
                .send(client);
        Assert.assertTrue(ltra.isSuccess());
        Assert.assertEquals(ltra.getTargets().length, 1);
        Assert.assertTrue(ltra.getTargets()[0].isXsd());
        String targetId = ltra.getTargets()[0].getTargetId();
        // addRequest
        /*
        Identity user = new Identity();
        LocalizedMultiValuedString cn = new LocalizedMultiValuedString();
        LocalizedStrings ss = new LocalizedStrings();
        ss.setLocale("en");
        ss.getValue().add("Ricardo Martin");
        cn.getValues().add(ss);
        user.setCommonName(cn);
        user.setCountryName("Spain");
        LocalizedSingleValuedString displayName = new LocalizedSingleValuedString();
        LocalizedString s = new LocalizedString();
        s.setValue("Ricardo Martin");
        displayName.getValue().add(s);
        user.setDisplayName(displayName);
        user.setEmployeeNumber("333");
        MultiValuedBinary password = new MultiValuedBinary();
        password.getValue().add("Kiosko_00".getBytes());
        user.setPassword(password);
        user.setSurname(cn);
        LocalizedMultiValuedString empType = new LocalizedMultiValuedString();
        ss = new LocalizedStrings();
        ss.getValue().add("Tecnico Rayos");
        empType.getValues().add(ss);
        user.setEmployeeType(empType);
        AddResponseAccessor ara = RequestBuilder.builderForAdd()
                .asynchronous()
                .targetId(targetId)
                .returnData()
                .xsdObject(psoObjectFactory.createIdentity(user))
                .send(client);
                */
        LookupResponseAccessor lra = RequestBuilder.builderForLookup()
                .synchronous()
                .psoId("identity:670")
                .returnData()
                .send(client);
    }
}
