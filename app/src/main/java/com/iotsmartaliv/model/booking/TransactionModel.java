package com.iotsmartaliv.model.booking;

import com.google.gson.annotations.JsonAdapter;

import java.util.Map;

public class TransactionModel {
    private String id;
    private String object;
    private int amount;
    private int amount_captured;
    private int amount_received;
    private int amount_refunded;
    private String application;
    private String application_fee;
    private String application_fee_amount;
    private String balance_transaction;
    private BillingDetails billing_details;
    private String calculated_statement_descriptor;
    private boolean captured;
    private long created;
    private String currency;
    private String customer;
    private String description;
    private String destination;
    private String dispute;
    private boolean disputed;
    private String failure_balance_transaction;
    private String failure_code;
    private String failure_message;
    private String invoice;
    private boolean livemode;

    @JsonAdapter(MetadataDeserializer.class)
    private Map<String, String> metadata;
    private String on_behalf_of;
    private String order;
    private Outcome outcome;
    private boolean paid;
    private String payment_intent;
    private String payment_method;
    private PaymentMethodDetails payment_method_details;
    private String receipt_email;
    private String receipt_number;
    private String receipt_url;
    private boolean refunded;
    private String review;
    private String shipping;
    private String source;
    private String source_transfer;
    private String statement_descriptor;
    private String statement_descriptor_suffix;
    private String status;
    private String transfer_data;
    private String transfer_group;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getAmount_captured() {
        return amount_captured;
    }

    public void setAmount_captured(int amount_captured) {
        this.amount_captured = amount_captured;
    }

    public int getAmount_refunded() {
        return amount_refunded;
    }

    public void setAmount_refunded(int amount_refunded) {
        this.amount_refunded = amount_refunded;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public String getApplication_fee() {
        return application_fee;
    }

    public void setApplication_fee(String application_fee) {
        this.application_fee = application_fee;
    }

    public String getApplication_fee_amount() {
        return application_fee_amount;
    }

    public void setApplication_fee_amount(String application_fee_amount) {
        this.application_fee_amount = application_fee_amount;
    }

    public String getBalance_transaction() {
        return balance_transaction;
    }

    public void setBalance_transaction(String balance_transaction) {
        this.balance_transaction = balance_transaction;
    }

    public BillingDetails getBilling_details() {
        return billing_details;
    }

    public void setBilling_details(BillingDetails billing_details) {
        this.billing_details = billing_details;
    }

    public String getCalculated_statement_descriptor() {
        return calculated_statement_descriptor;
    }

    public void setCalculated_statement_descriptor(String calculated_statement_descriptor) {
        this.calculated_statement_descriptor = calculated_statement_descriptor;
    }

    public boolean isCaptured() {
        return captured;
    }

    public void setCaptured(boolean captured) {
        this.captured = captured;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDispute() {
        return dispute;
    }

    public void setDispute(String dispute) {
        this.dispute = dispute;
    }

    public boolean isDisputed() {
        return disputed;
    }

    public void setDisputed(boolean disputed) {
        this.disputed = disputed;
    }

    public String getFailure_balance_transaction() {
        return failure_balance_transaction;
    }

    public void setFailure_balance_transaction(String failure_balance_transaction) {
        this.failure_balance_transaction = failure_balance_transaction;
    }

    public String getFailure_code() {
        return failure_code;
    }

    public void setFailure_code(String failure_code) {
        this.failure_code = failure_code;
    }

    public String getFailure_message() {
        return failure_message;
    }

    public void setFailure_message(String failure_message) {
        this.failure_message = failure_message;
    }

    public String getInvoice() {
        return invoice;
    }

    public void setInvoice(String invoice) {
        this.invoice = invoice;
    }

    public boolean isLivemode() {
        return livemode;
    }

    public void setLivemode(boolean livemode) {
        this.livemode = livemode;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }

    public String getOn_behalf_of() {
        return on_behalf_of;
    }

    public void setOn_behalf_of(String on_behalf_of) {
        this.on_behalf_of = on_behalf_of;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public Outcome getOutcome() {
        return outcome;
    }

    public void setOutcome(Outcome outcome) {
        this.outcome = outcome;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public String getPayment_intent() {
        return payment_intent;
    }

    public void setPayment_intent(String payment_intent) {
        this.payment_intent = payment_intent;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }

    public PaymentMethodDetails getPayment_method_details() {
        return payment_method_details;
    }

    public void setPayment_method_details(PaymentMethodDetails payment_method_details) {
        this.payment_method_details = payment_method_details;
    }

    public String getReceipt_email() {
        return receipt_email;
    }

    public void setReceipt_email(String receipt_email) {
        this.receipt_email = receipt_email;
    }

    public String getReceipt_number() {
        return receipt_number;
    }

    public void setReceipt_number(String receipt_number) {
        this.receipt_number = receipt_number;
    }

    public String getReceipt_url() {
        return receipt_url;
    }

    public void setReceipt_url(String receipt_url) {
        this.receipt_url = receipt_url;
    }

    public boolean isRefunded() {
        return refunded;
    }

    public void setRefunded(boolean refunded) {
        this.refunded = refunded;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getShipping() {
        return shipping;
    }

    public void setShipping(String shipping) {
        this.shipping = shipping;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSource_transfer() {
        return source_transfer;
    }

    public void setSource_transfer(String source_transfer) {
        this.source_transfer = source_transfer;
    }

    public String getStatement_descriptor() {
        return statement_descriptor;
    }

    public void setStatement_descriptor(String statement_descriptor) {
        this.statement_descriptor = statement_descriptor;
    }

    public String getStatement_descriptor_suffix() {
        return statement_descriptor_suffix;
    }

    public void setStatement_descriptor_suffix(String statement_descriptor_suffix) {
        this.statement_descriptor_suffix = statement_descriptor_suffix;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTransfer_data() {
        return transfer_data;
    }

    public void setTransfer_data(String transfer_data) {
        this.transfer_data = transfer_data;
    }

    public String getTransfer_group() {
        return transfer_group;
    }

    public void setTransfer_group(String transfer_group) {
        this.transfer_group = transfer_group;
    }

    public int getAmount_received() {
        return amount_received;
    }

    public void setAmount_received(int amount_received) {
        this.amount_received = amount_received;
    }
}
