//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.01.30 at 06:59:18 PM CET 
//


package es.rickyepoderi.spml4jaxb.test.oim;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the oracle.iam.wsschema.model.common.pso package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _RoleWithRelations_QNAME = new QName("http://xmlns.oracle.com/idm/identity/PSO", "roleWithRelations");
    private final static QName _Member_QNAME = new QName("http://xmlns.oracle.com/idm/identity/PSO", "member");
    private final static QName _IdentityWithRelations_QNAME = new QName("http://xmlns.oracle.com/idm/identity/PSO", "identityWithRelations");
    private final static QName _Role_QNAME = new QName("http://xmlns.oracle.com/idm/identity/PSO", "role");
    private final static QName _Identity_QNAME = new QName("http://xmlns.oracle.com/idm/identity/PSO", "identity");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: oracle.iam.wsschema.model.common.pso
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Member }
     * 
     */
    public Member createMember() {
        return new Member();
    }

    /**
     * Create an instance of {@link IdentityWithRelations }
     * 
     */
    public IdentityWithRelations createIdentityWithRelations() {
        return new IdentityWithRelations();
    }

    /**
     * Create an instance of {@link RoleWithRelations }
     * 
     */
    public RoleWithRelations createRoleWithRelations() {
        return new RoleWithRelations();
    }

    /**
     * Create an instance of {@link Identity }
     * 
     */
    public Identity createIdentity() {
        return new Identity();
    }

    /**
     * Create an instance of {@link Role }
     * 
     */
    public Role createRole() {
        return new Role();
    }

    /**
     * Create an instance of {@link LocalizedSingleValuedString }
     * 
     */
    public LocalizedSingleValuedString createLocalizedSingleValuedString() {
        return new LocalizedSingleValuedString();
    }

    /**
     * Create an instance of {@link LocalizedString }
     * 
     */
    public LocalizedString createLocalizedString() {
        return new LocalizedString();
    }

    /**
     * Create an instance of {@link MultiValuedString }
     * 
     */
    public MultiValuedString createMultiValuedString() {
        return new MultiValuedString();
    }

    /**
     * Create an instance of {@link TelephoneNumbers }
     * 
     */
    public TelephoneNumbers createTelephoneNumbers() {
        return new TelephoneNumbers();
    }

    /**
     * Create an instance of {@link DsmlAttr }
     * 
     */
    public DsmlAttr createDsmlAttr() {
        return new DsmlAttr();
    }

    /**
     * Create an instance of {@link ProvisioningObjectType }
     * 
     */
    public ProvisioningObjectType createProvisioningObjectType() {
        return new ProvisioningObjectType();
    }

    /**
     * Create an instance of {@link MultiValuedBinary }
     * 
     */
    public MultiValuedBinary createMultiValuedBinary() {
        return new MultiValuedBinary();
    }

    /**
     * Create an instance of {@link LocalizedMultiValuedString }
     * 
     */
    public LocalizedMultiValuedString createLocalizedMultiValuedString() {
        return new LocalizedMultiValuedString();
    }

    /**
     * Create an instance of {@link UnboundedAttributes }
     * 
     */
    public UnboundedAttributes createUnboundedAttributes() {
        return new UnboundedAttributes();
    }

    /**
     * Create an instance of {@link LocalizedStrings }
     * 
     */
    public LocalizedStrings createLocalizedStrings() {
        return new LocalizedStrings();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RoleWithRelations }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://xmlns.oracle.com/idm/identity/PSO", name = "roleWithRelations")
    public JAXBElement<RoleWithRelations> createRoleWithRelations(RoleWithRelations value) {
        return new JAXBElement<RoleWithRelations>(_RoleWithRelations_QNAME, RoleWithRelations.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Member }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://xmlns.oracle.com/idm/identity/PSO", name = "member")
    public JAXBElement<Member> createMember(Member value) {
        return new JAXBElement<Member>(_Member_QNAME, Member.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IdentityWithRelations }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://xmlns.oracle.com/idm/identity/PSO", name = "identityWithRelations")
    public JAXBElement<IdentityWithRelations> createIdentityWithRelations(IdentityWithRelations value) {
        return new JAXBElement<IdentityWithRelations>(_IdentityWithRelations_QNAME, IdentityWithRelations.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Role }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://xmlns.oracle.com/idm/identity/PSO", name = "role")
    public JAXBElement<Role> createRole(Role value) {
        return new JAXBElement<Role>(_Role_QNAME, Role.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Identity }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://xmlns.oracle.com/idm/identity/PSO", name = "identity")
    public JAXBElement<Identity> createIdentity(Identity value) {
        return new JAXBElement<Identity>(_Identity_QNAME, Identity.class, null, value);
    }

}
