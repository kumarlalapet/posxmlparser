package com.mapr.labs;

import org.nrf_arts.ixretail.namespace.*;

import javax.xml.bind.JAXBException;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Created by mlalapet on 11/3/15.
 */
public class POSXMLParserTest {
    public static void main(String args[]) throws JAXBException {
        String xml = covertToXMLTest();

        POSLogRoot pos = POSXMLParser.parseXML(xml);
        System.out.println(pos);
        System.out.println(pos.getTransaction().get(0).getWorkstationID().getValue());
    }

    private static String covertToXMLTest() throws JAXBException {
        POSLogRoot poslog = new POSLogRoot();

        TransactionDomainSpecific transaction = new TransactionDomainSpecific();
        WorkstationIDCommonData ws = new WorkstationIDCommonData();
        ws.setValue("POS5");
        transaction.setWorkstationID(ws);
        transaction.setSequenceNumber(new BigInteger("4294967295"));

        RetailTransactionDomainSpecific retailTransaction = new RetailTransactionDomainSpecific();
        retailTransaction.setItemCount(new Long(1));

        LineItemDomainSpecific lineItem = new LineItemDomainSpecific();
        PointsBase psb = new PointsBase();
        psb.setPointRate(new BigDecimal("10"));
        lineItem.setPoints(psb);

        SaleBase sale = new SaleBase();
        sale.setItemType("stock");
        ActualSalesUnitPriceType asupt = new ActualSalesUnitPriceType();
        asupt.setValue(new BigDecimal("1.63"));
        sale.setActualSalesUnitPrice(asupt);
        ExtendedAmountType eat = new ExtendedAmountType();
        eat.setValue(new BigDecimal("4.89"));
        sale.setExtendedAmount(eat);
        QuantityCommonData qcd = new QuantityCommonData();
        qcd.setValue(new BigDecimal("3"));
        sale.getQuantity().add(qcd);

        RetailTransactionPOSIdentity posIdentity = new RetailTransactionPOSIdentity();
        posIdentity.setPOSItemID("ITEM1234567");

        sale.getPOSIdentityOrItemIDOrSpecialOrderNumber().add(posIdentity);

        lineItem.setSale(sale);

        retailTransaction.getLineItem().add(lineItem);

        LineItemDomainSpecific cdlineItem = new LineItemDomainSpecific();

        TenderDomainSpecific tender = new TenderDomainSpecific();
        POSLogTenderID tenderid = new POSLogTenderID();
        tenderid.setTenderType("CreditDebit");
        tender.setTenderID(tenderid);
        tender.setTypeCode("Sale");
        TenderBase.Amount amount = new TenderBase.Amount();
        amount.setValue(new BigDecimal("4.89"));
        tender.setAmount(amount);

        TenderAuthorizationDomainSpecific authorization = new TenderAuthorizationDomainSpecific();
        AmountCommonData amountt = new AmountCommonData();
        amountt.setValue(new BigDecimal("4.89"));
        authorization.setRequestedAmount(amountt);
        authorization.setAuthorizationCode("234");
        authorization.setReferenceNumber("123");
        tender.getAuthorization().add(authorization);

        TenderCreditDebitDomainSpecific creditdebit = new TenderCreditDebitDomainSpecific();
        creditdebit.setCardType("Debit");
        creditdebit.setIssuerIdentificationNumber(new BigInteger("1234567887654321"));
        creditdebit.setCardHolderName("M Lalapet");
        EncryptedDataType pan = new EncryptedDataType();
        pan.setValue("1234567890");
        creditdebit.setPrimaryAccountNumber(pan);
        tender.getCreditDebit().add(creditdebit);

        cdlineItem.setTender(tender);
        retailTransaction.getLineItem().add(cdlineItem);

        TransactionTotalBase total = new TransactionTotalBase();
        total.setTotalType("TransactionGrandAmount");
        total.setValue(new BigDecimal("4.89"));
        retailTransaction.getTotal().add(total);

        transaction.getRetailTransaction().add(retailTransaction);

        poslog.getTransaction().add(transaction);

        String result = POSXMLParser.convertToXML(poslog);
        System.out.println(result);

        return result;
    }
}
