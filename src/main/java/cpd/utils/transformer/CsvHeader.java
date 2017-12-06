package cpd.utils.transformer;

import cpd.utils.helper.CsvCreator;

public enum CsvHeader {
  ID, FROM, TO, DESCR, TYPE, ELIGIBILITY, PLU, REBATE, COUPON, CUSTOMER_GROUP, MERCH_GROUP;
  public static String HEADER = CsvCreator.createLine((Object[]) CsvHeader.values()) + '\n';
}