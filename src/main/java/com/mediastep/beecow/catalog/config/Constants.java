package com.mediastep.beecow.catalog.config;

/**
 * Application constants.
 */
public final class Constants {

    public static final String SYSTEM_ACCOUNT = "system";
    public static final String COUNTRY_CODE_VN = "VN";

    public static class CacheName {
        public static final String CATALOG_COUNTRY_BY_CODE = "catalogCountryByCode";
        public static final String CATALOG_COUNTRY_WITH_CITIES_BY_CODE = "catalogCountryWithCitesByCode";
        public static final String CATALOG_COUNTRY_ALL = "catalogCountryAll";
        public static final String CATALOG_COUNTRY_BY_ID = "catalogCountryById";
        public static final String CATALOG_COUNTRY_BY_ADDRESS = "catalogCountryAddress";



        public static final String CATALOG_CITY_ALL = "catalogCityAll";
        public static final String CATALOG_CITY_BY_CODE = "catalogCityByCode";
        public static final String CATALOG_CITY_BY_ID = "catalogCityById";
        public static final String CATALOG_CITY_BY_COUNTRY_CODE = "catalogCityByCountryCode";
        public static final String CATALOG_CITY_BY_COUNTRY_CODE_AND_KEYWORD = "catalogCityByCountryCodeAndKeyword";
        public static final String CATALOG_CITY_BY_COUNTRY_CODE_AND_CODE = "catalogCityByCountryCodeAndCode";
        public static final String CATALOG_CITY_BY_COUNTRY_CODE_AND_NAME = "catalogCityByCountryCodeAndName";
        public static final String CATALOG_CITY_BY_COUNTRY_CODE_AND_NAME_OR_NAME = "catalogCityByCountryCodeAndNameOrName";
        public static final String CATALOG_CITY_TREE_BY_COUNTRY_CODE = "catalogCityTreeByCountryCode";

        public static final String CATALOG_DISTRICT_ALL = "catalogDistrictAll";
        public static final String CATALOG_DISTRICT_BY_COUNTRY = "catalogDistrictsByCountry";
        public static final String CATALOG_DISTRICT_BY_CITY_CODE = "catalogDistrictByCityCode";
        public static final String CATALOG_DISTRICT_BY_CITY_CODE_AND_NAME = "catalogDistrictByCityCodeAndName";
        public static final String CATALOG_DISTRICT_BY_CITY_AND_CODE_OR_NAME = "catalogDistrictByCityAndCodeOrName";
        public static final String CATALOG_GET_WARD_BY_NAME_AND_COUNTRY = "getAllWardByNameAndCountry";
        public static final String CATALOG_GET_DISTRICT_BY_NAME_AND_COUNTRY = "getAllDistrictByNameAndCountry";

        public static final String CATALOG_WARD_ALL = "catalogWardAll";
        public static final String CATALOG_WARD_BY_COUNTRY = "catalogWardsByCountry";
        public static final String CATALOG_WARD_BY_DISTRICT_CODE = "catalogWardByDistrictCode";
        public static final String CATALOG_WARD_BY_DISTRICT_CODE_AND_NAME = "catalogWardByDistrictCodeAndName";
        public static final String CATALOG_WARD_BY_DISTRICT_AND_CODE_OR_NAME = "catalogWardByDistrictAndCodeOrName";

        public static final String CATALOG_TERM_BY_TAXONOMY_AND_LEVEL = "catalogTermByTaxonomyAndLevel";
        public static final String CATALOG_TERM_AND_CHILDREN_BY_TAXONOMY_AND_METADATA = "catalogTermAndChildrenByTaxonomyAndMetadata";
        public static final String CATALOG_TERM = "terms";
        public static final String CATALOG_TERM_TREE = "terms-tree";

        public static final String CATALOG_PACKAGE_CURRENCY_BY_CURRENCY_CODE = "catalogPackageCurrencyByCurrencyCode";
        public static final String CATALOG_PACKAGE_CURRENCY_BY_LOCALE = "catalogPackageCurrencyByLocale";
    }

    private Constants() {
    }
}
