package site.model;


import site.config.Globals;
import site.controller.epay.EpayResponse;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;
import java.util.ArrayList;
import java.util.List;

/**
 * A legal entity (or person) that BUYS the tickets.
 *
 * @author Mihail Stoynov
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Registrant extends AbstractEntity {

    @OneToMany(mappedBy = "registrant", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Visitor> visitors = new ArrayList<>();
    private boolean isCompany = true;
    private boolean isStudent;
    private String name = "";
    private String address;
    private String vatNumber;
    private String mol;
    private String email = "";
    private String eik;
//    @Generated(GenerationTime.INSERT)
    @Column(unique = false)
    private Long epayInvoiceNumber;//the one for epay
    @Column(unique = false)//because initially it is zero
    private Long realInvoiceNumber;//the real one, only after they pay
    @Column(unique = false)//because might not always be initialized
    private Long proformaInvoiceNumber;
    private PaymentType paymentType;
    @Enumerated(EnumType.STRING)
    private Branch branch = Globals.CURRENT_BRANCH;

    @Embedded
    private EpayResponse epayResponse;
    
    @Transient
    private String captcha;

    public enum PaymentType {
        EPAY_ACCOUNT("ePay account", "epay.bg"), EPAY_CREDIT_CARD("Bank card", "Банкова карта"),
        BANK_TRANSFER("Direct bank transfer", "Банков превод");

        private final String value;
        private final String bulgarianValue;

        PaymentType(String theValue, String bulgarianValue) {
            this.value = theValue;
            this.bulgarianValue = bulgarianValue;
        }

        public String toString() {
            return value;
        }

        public String getBulgarianValue() {
            return bulgarianValue;
        }
    }

    @MappedSuperclass
    public static class NumberGenerator {
        @Id
        @GeneratedValue
        private int id;
        private long counter;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public long getCounter() {
            return counter;
        }

        public void setCounter(long counter) {
            this.counter = counter;
        }
    }

    /** the entity that generates the unique invoice numbers for epay */
    @Entity
    public static class EpayInvoiceNumberGenerator extends NumberGenerator {
    }

    /**
     * The {@link site.model.Registrant.EpayInvoiceNumberGenerator} generates unique invoice numbers, but one user can
     * generate too many of these. I have decided to implement another invoice generator, that generates a number only
     * wneh a user pays up. The REAL invoice generator. This number is different from the one from the epay generator.
     */
    @Entity
    public static class RealInvoiceNumberGenerator extends NumberGenerator{
    }

    /** Generates proforma invoice numbers. */
    @Entity
    public static class ProformaInvoiceNumberGenerator extends NumberGenerator{
    }

    public Registrant() {
    }

    public Registrant(String name, String email) {
        this(false, name, null, null, null, email);
    }

    public Registrant(boolean isCompany, String name, String address, String vatNumber, String mol, String email) {
        this(isCompany, name, address, vatNumber, vatNumber, mol, email, PaymentType.BANK_TRANSFER);
    }

    public Registrant(boolean isCompany, String name, String address, String vatNumber, String eik, String mol, String email, PaymentType paymentType) {
        this.isCompany = isCompany;
        this.name = name;
        this.address = address;
        this.vatNumber = vatNumber;
        this.eik = eik;
        this.mol = mol;
        this.email = email;
        this.paymentType = paymentType;
    }

    public List<Visitor> getVisitors() {
        return visitors;
    }

    public void setVisitors(List<Visitor> visitors) {
        this.visitors = visitors;
    }

    public boolean isCompany() {
        return isCompany;
    }

    public void setCompany(boolean isCompany) {
        this.isCompany = isCompany;
    }

    public boolean isStudent() {
        return isStudent;
    }

    public void setStudent(boolean student) {
        isStudent = student;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getVatNumber() {
        if (vatNumber != null && !vatNumber.isEmpty() && !vatNumber.startsWith("BG")) {
            return "BG" + vatNumber;
        }
        return vatNumber;
    }

    public void setVatNumber(String vatNumber) {
        this.vatNumber = vatNumber;
    }

    public String getMol() {
        return mol;
    }

    public void setMol(String mol) {
        this.mol = mol;
    }

    public String getEik() {
        return eik;
    }

    public void setEik(String eik) {
        this.eik = eik;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getEpayInvoiceNumber() {

        return epayInvoiceNumber==null?Long.valueOf(0):epayInvoiceNumber;
    }

    public void setEpayInvoiceNumber(long epayInvoiceNumber) {
        this.epayInvoiceNumber = epayInvoiceNumber;
    }

    public Long getRealInvoiceNumber() {
        return realInvoiceNumber==null?Long.valueOf(0):realInvoiceNumber;
    }

    public void setRealInvoiceNumber(long realInvoiceNumber) {
        this.realInvoiceNumber = realInvoiceNumber;
    }

    public Long getProformaInvoiceNumber() {
        return proformaInvoiceNumber==null?Long.valueOf(0):proformaInvoiceNumber;
    }

    public void setProformaInvoiceNumber(Long proformaInvoiceNumber) {
        this.proformaInvoiceNumber = proformaInvoiceNumber;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public EpayResponse getEpayResponse() {
        return epayResponse;
    }

    public void setEpayResponse(EpayResponse epayResponse) {
        this.epayResponse = epayResponse;
    }

    public Branch getBranch() {
		return branch;
	}

	public void setBranch(Branch branch) {
		this.branch = branch;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Registrant)) {
            return false;
        }

        Registrant that = (Registrant) o;

        if (isCompany != that.isCompany) {
            return false;
        }
        if (address != null ? !address.equals(that.address) : that.address != null) {
            return false;
        }
        if (!email.equals(that.email)) {
            return false;
        }
        if (mol != null ? !mol.equals(that.mol) : that.mol != null) {
            return false;
        }
        if (!name.equals(that.name)) {
            return false;
        }
        return !(vatNumber != null ? !vatNumber.equals(that.vatNumber) : that.vatNumber != null);
    }

    @Override
    public int hashCode() {
        int result = isCompany ? 1 : 0;
        result = 31 * result + name.hashCode();
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (vatNumber != null ? vatNumber.hashCode() : 0);
        result = 31 * result + (mol != null ? mol.hashCode() : 0);
        result = 31 * result + email.hashCode();
        return result;
    }

	public String getCaptcha() {
		return captcha;
	}

	public void setCaptcha(String captcha) {
		this.captcha = captcha;
	}
}