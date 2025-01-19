package andriawan.takehome.test.entities;

public class DebtHistory {
    private String id;
    private String crediturName;
    private String debiturName;
    private Long amount;

    // Constructor
    public DebtHistory(String id, String crediturName, String debiturName, Long amount) {
        this.id = id;
        this.crediturName = crediturName;
        this.debiturName = debiturName;
        this.amount = amount;
    }

    // Getter and Setter for id
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    // Getter and Setter for crediturName
    public String getCrediturName() {
        return crediturName;
    }

    public void setCrediturName(String crediturName) {
        this.crediturName = crediturName;
    }

    // Getter and Setter for debiturName
    public String getDebiturName() {
        return debiturName;
    }

    public void setDebiturName(String debiturName) {
        this.debiturName = debiturName;
    }

    // Getter and Setter for amount
    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }
}
