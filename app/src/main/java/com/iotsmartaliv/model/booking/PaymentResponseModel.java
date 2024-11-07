package com.iotsmartaliv.model.booking;

import com.google.gson.annotations.JsonAdapter;

import java.util.List;
import java.util.Map;

public class PaymentResponseModel {
    public int statusCode;
    public String msg;
    public PaymentIntent amountIntent;
    public PaymentIntent depositIntent;
    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public PaymentIntent getAmountIntent() {
        return amountIntent;
    }

    public void setAmountIntent(PaymentIntent amountIntent) {
        this.amountIntent = amountIntent;
    }

    public PaymentIntent getDepositIntent() {
        return depositIntent;
    }

    public void setDepositIntent(PaymentIntent depositIntent) {
        this.depositIntent = depositIntent;
    }

    public class PaymentIntent {
        public String id;
        public String object;
        public int amount;
        public int amount_capturable;
        public AmountDetails amount_details;
        public int amount_received;
        public String application;
        public String application_fee_amount;
        public AutomaticPaymentMethods automatic_payment_methods;
        public String canceled_at;
        public String cancellation_reason;
        public String capture_method;
        public String client_secret;
        public String confirmation_method;
        public int created;
        public String currency;
        public String customer;
        public String description;
        public String invoice;
        public String last_payment_error;
        public String latest_charge;
        public boolean livemode;
        @JsonAdapter(MetadataDeserializer.class)
        private Map<String, String> metadata;
        public String next_action;
        public String on_behalf_of;
        public String payment_method;
        public String payment_method_configuration_details;
        public PaymentMethodOptions payment_method_options;
        public List<String> payment_method_types;
        public String processing;
        public String receipt_email;
        public String review;
        public String setup_future_usage;
        public String shipping;
        public String source;
        public String statement_descriptor;
        public String statement_descriptor_suffix;
        public String status;
        public String transfer_data;
        public String transfer_group;

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

        public int getAmount_capturable() {
            return amount_capturable;
        }

        public void setAmount_capturable(int amount_capturable) {
            this.amount_capturable = amount_capturable;
        }

        public AmountDetails getAmount_details() {
            return amount_details;
        }

        public void setAmount_details(AmountDetails amount_details) {
            this.amount_details = amount_details;
        }

        public int getAmount_received() {
            return amount_received;
        }

        public void setAmount_received(int amount_received) {
            this.amount_received = amount_received;
        }

        public String getApplication() {
            return application;
        }

        public void setApplication(String application) {
            this.application = application;
        }

        public String getApplication_fee_amount() {
            return application_fee_amount;
        }

        public void setApplication_fee_amount(String application_fee_amount) {
            this.application_fee_amount = application_fee_amount;
        }

        public AutomaticPaymentMethods getAutomatic_payment_methods() {
            return automatic_payment_methods;
        }

        public void setAutomatic_payment_methods(AutomaticPaymentMethods automatic_payment_methods) {
            this.automatic_payment_methods = automatic_payment_methods;
        }

        public String getCanceled_at() {
            return canceled_at;
        }

        public void setCanceled_at(String canceled_at) {
            this.canceled_at = canceled_at;
        }

        public String getCancellation_reason() {
            return cancellation_reason;
        }

        public void setCancellation_reason(String cancellation_reason) {
            this.cancellation_reason = cancellation_reason;
        }

        public String getCapture_method() {
            return capture_method;
        }

        public void setCapture_method(String capture_method) {
            this.capture_method = capture_method;
        }

        public String getClient_secret() {
            return client_secret;
        }

        public void setClient_secret(String client_secret) {
            this.client_secret = client_secret;
        }

        public String getConfirmation_method() {
            return confirmation_method;
        }

        public void setConfirmation_method(String confirmation_method) {
            this.confirmation_method = confirmation_method;
        }

        public int getCreated() {
            return created;
        }

        public void setCreated(int created) {
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

        public String getInvoice() {
            return invoice;
        }

        public void setInvoice(String invoice) {
            this.invoice = invoice;
        }

        public String getLast_payment_error() {
            return last_payment_error;
        }

        public void setLast_payment_error(String last_payment_error) {
            this.last_payment_error = last_payment_error;
        }

        public String getLatest_charge() {
            return latest_charge;
        }

        public void setLatest_charge(String latest_charge) {
            this.latest_charge = latest_charge;
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

        public String getNext_action() {
            return next_action;
        }

        public void setNext_action(String next_action) {
            this.next_action = next_action;
        }

        public String getOn_behalf_of() {
            return on_behalf_of;
        }

        public void setOn_behalf_of(String on_behalf_of) {
            this.on_behalf_of = on_behalf_of;
        }

        public String getPayment_method() {
            return payment_method;
        }

        public void setPayment_method(String payment_method) {
            this.payment_method = payment_method;
        }

        public String getPayment_method_configuration_details() {
            return payment_method_configuration_details;
        }

        public void setPayment_method_configuration_details(String payment_method_configuration_details) {
            this.payment_method_configuration_details = payment_method_configuration_details;
        }

        public PaymentMethodOptions getPayment_method_options() {
            return payment_method_options;
        }

        public void setPayment_method_options(PaymentMethodOptions payment_method_options) {
            this.payment_method_options = payment_method_options;
        }

        public List<String> getPayment_method_types() {
            return payment_method_types;
        }

        public void setPayment_method_types(List<String> payment_method_types) {
            this.payment_method_types = payment_method_types;
        }

        public String getProcessing() {
            return processing;
        }

        public void setProcessing(String processing) {
            this.processing = processing;
        }

        public String getReceipt_email() {
            return receipt_email;
        }

        public void setReceipt_email(String receipt_email) {
            this.receipt_email = receipt_email;
        }

        public String getReview() {
            return review;
        }

        public void setReview(String review) {
            this.review = review;
        }

        public String getSetup_future_usage() {
            return setup_future_usage;
        }

        public void setSetup_future_usage(String setup_future_usage) {
            this.setup_future_usage = setup_future_usage;
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
    }

    public class PaymentMethodOptions {
        public Card card;


    }

    public class Card {
        public String installments;
        public String mandate_options;
        public String network;
        public String request_three_d_secure;

        public String getInstallments() {
            return installments;
        }

        public void setInstallments(String installments) {
            this.installments = installments;
        }

        public String getMandate_options() {
            return mandate_options;
        }

        public void setMandate_options(String mandate_options) {
            this.mandate_options = mandate_options;
        }

        public String getNetwork() {
            return network;
        }

        public void setNetwork(String network) {
            this.network = network;
        }

        public String getRequest_three_d_secure() {
            return request_three_d_secure;
        }

        public void setRequest_three_d_secure(String request_three_d_secure) {
            this.request_three_d_secure = request_three_d_secure;
        }
    }

    public class AutomaticPaymentMethods {
        public String allow_redirects;
        public boolean enabled;

        public String getAllow_redirects() {
            return allow_redirects;
        }

        public void setAllow_redirects(String allow_redirects) {
            this.allow_redirects = allow_redirects;
        }

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }

    public class AmountDetails {
        public List<Object> tip;

        public List<Object> getTip() {
            return tip;
        }

        public void setTip(List<Object> tip) {
            this.tip = tip;
        }
    }

}
