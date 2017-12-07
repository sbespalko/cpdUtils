package cpd.utils.transformer;

import cpd.utils.helper.CsvCreator;

public enum CsvHeader {
  ID, FROM, TO, DESC, TYPE, ELIGIBILITY, PLU, REBATE, TO_BE_PAID_QUANTITY, COUPON, CUSTOMER_GROUP, MERCH_GROUP;
  public static String HEADER = CsvCreator.createLine((Object[]) CsvHeader.values()) + '\n';
}