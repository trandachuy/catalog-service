package com.mediastep.beecow.catalog.service.impl;

import com.google.gson.Gson;
import com.mediastep.beecow.catalog.dto.AddressDTO;
import com.mediastep.beecow.catalog.dto.CitySimpleDTO;
import com.mediastep.beecow.catalog.dto.DistrictDTO;
import com.mediastep.beecow.catalog.dto.WardNewDTO;
import com.mediastep.beecow.catalog.repository.DistrictNativeRepository;
import com.mediastep.beecow.catalog.repository.WardNativeRepository;
import com.mediastep.beecow.catalog.service.*;
import com.mediastep.beecow.catalog.service.dto.FullShippingAddressDTO;
import com.mediastep.beecow.catalog.service.mapper.DistrictMapper;
import com.mediastep.beecow.catalog.service.mapper.WardMapper;
import com.mediastep.beecow.catalog.web.rest.vm.CountryVM;
import com.mediastep.beecow.catalog.web.rest.vm.FullAddressVM;
import com.mediastep.beecow.common.dto.CityDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private CountryService countryService;
    @Autowired
    private CityService cityService;
    @Autowired
    private DistrictService districtService;
    @Autowired
    private WardService wardService;
    @Autowired
    private Gson gson;
    @Autowired
    private DistrictNativeRepository districtNativeRepository;
    @Autowired
    private WardNativeRepository wardNativeRepository;
    @Autowired
    private DistrictMapper districtMapper;
    @Autowired
    private WardMapper wardMapper;

    /*
    *   @SerializedName("address_line_1") // maybe street or full address
        private String addressLine1;
        @SerializedName("address_line_2")// maybe ward
        private String addressLine2;
        @SerializedName("address_line_3")
        private String addressLine3;
        @SerializedName("admin_area_1") //maybe city
        private String adminArea1;
        @SerializedName("admin_area_2") // maybe district
        private String adminArea2;
        @SerializedName("admin_area_3")
        private String adminArea3;
        @SerializedName("admin_area_4")
        private String adminArea4;
        @SerializedName("country_code") //country
        private String countryCode;
        @SerializedName("postal_code")
        private String postalCode;
    *
    * */
    @Override
    public Set<AddressDTO> convertString2Code(AddressDTO address) {
        log.debug("search address {}", gson.toJson(address));
        if (Objects.isNull(address) || Objects.isNull(address.getCountryCode())) {
            return new HashSet<>();
        }
        String countryCode = address.getCountryCode();
        String lang = countryCode.equalsIgnoreCase("vn") ? "vi" : "en";
        String city = address.getAdminArea1();
        String district = address.getAdminArea2();
        String ward = address.getAddressLine2();
        String streetOrFullAddress = address.getAddressLine1();
        String postCode = address.getPostalCode();
        CountryVM country = this.countryService.findOneByCode(countryCode, true);
        Long countryId = Objects.nonNull(country) ? country.getId() : null;
        List<CityDTO> cities = new ArrayList<>();
        List<DistrictDTO> districts = new ArrayList<>();
        List<WardNewDTO> wards = new ArrayList<>();
        if (Objects.nonNull(countryId)) {
            //search: city -> district -> ward -> ...
            if (!StringUtils.isEmpty(city)) {
                log.debug("search: city -> district -> ward -> ...");
                cities = cityService.searchCityByCountryAndCodeOrName(new HashSet<>(Collections.singletonList(countryId)), city);
                Set<Long> cityIds = cities.parallelStream().map(CityDTO::getId).collect(Collectors.toSet());
                districts = districtService.searchDistrictsByCityAndCodeOrName(new HashSet<>(cityIds), district);
                Set<Long> districtIds = districts.parallelStream().map(DistrictDTO::getId).collect(Collectors.toSet());
                wards = wardService.searchWardByDistrictAndCodeOrName(districtIds, ward);

            }
            //search: district -> city -> ward -> ...
            else if (!StringUtils.isEmpty(district)) {
                log.debug("search: district -> city -> ward -> ...");
                districts = districtNativeRepository.getAllDistrictByNameAndCountry(countryId, district, lang).parallelStream().map(districtMapper::toDto).collect(Collectors.toList());
                if (!CollectionUtils.isEmpty(districts)) {
                    Set<Long> cityIds = districts.parallelStream().map(DistrictDTO::getCityId).filter(Objects::nonNull).collect(Collectors.toSet());
                    cities = cityService.findAllByIdIn(cityIds);
                }
                if (!StringUtils.isEmpty(ward)) {
                    wards = wardNativeRepository.getAllWardByNameAndCountry(countryId, ward, lang).parallelStream().map(wardMapper::toDto).collect(Collectors.toList());
                }

            }
            // search: ward -> district -> ...
            else if (StringUtils.isEmpty(city) && StringUtils.isEmpty(district) && !StringUtils.isEmpty(ward)){
                log.debug("ward -> district -> ...");
                wards = wardNativeRepository.getAllWardByNameAndCountry(countryId, ward, lang).parallelStream().map(wardMapper::toDto).collect(Collectors.toList());
                if (!CollectionUtils.isEmpty(wards)){
                    Set<Long> districtIds = wards.parallelStream().map(WardNewDTO::getDistrictId).filter(Objects::nonNull).collect(Collectors.toSet());
                    districts = districtService.findAllByIdIn(districtIds);
                }
            }
        }

        log.debug("citys:[size: {}, {}]", cities.size(), gson.toJson(cities));
        log.debug("districts:[size: {}, {}]", districts.size(), gson.toJson(districts));
        log.debug("wards:[size: {}, {}]", wards.size(), gson.toJson(wards));
        return this.map(cities, districts, wards, streetOrFullAddress, countryCode, postCode);
    }

    private Set<AddressDTO> map(List<CityDTO> cities, List<DistrictDTO> districts, List<WardNewDTO> wards, String streetOrFullAddress, String countryCode, String postCode) {
        Set<AddressDTO> adds = new HashSet<>();
        cities.parallelStream().filter(Objects::nonNull).forEach(c -> {
            Long cityId = c.getId();
            AddressDTO a = new AddressDTO();
            a.setAdminArea1(c.getCode());
            Optional<DistrictDTO> d = districts.parallelStream().filter(
                dtr -> Objects.nonNull(dtr) && Objects.nonNull(cityId) && Objects.equals(dtr.getCityId(), cityId)
            ).findFirst();
            d.ifPresent(dtr -> a.setAdminArea2(dtr.getCode()));
            Long districtId = d.map(DistrictDTO::getId).orElse(null);
            Optional<WardNewDTO> w = wards.parallelStream().filter(
                war -> Objects.nonNull(war) && Objects.nonNull(districtId) && Objects.equals(war.getDistrictId(), districtId)
            ).findFirst();
            w.ifPresent(wr -> a.setAddressLine2(wr.getCode()));
            a.setAddressLine1(streetOrFullAddress);
            a.setPostalCode(postCode);
            a.setCountryCode(countryCode);
            adds.add(a);
        });

        return adds;
    }

    @Override
    public FullShippingAddressDTO getFullShippingAddress(FullAddressVM request) {
        FullShippingAddressDTO rs = new FullShippingAddressDTO();
        StringBuilder sb = new StringBuilder();
        StringBuilder sbEn = new StringBuilder();
        if (!StringUtils.isEmpty(request.getCountryCode())) {
            if (request.getCountryCode().equals("VN")) {
                boolean flag = false;
                if (!StringUtils.isEmpty(request.getAddress1())) {
                    flag = true;
                    sb.append(request.getAddress1()).append(", ");
                    sbEn.append(request.getAddress1()).append(", ");
                }
                if (!StringUtils.isEmpty(request.getWardCode())) {
                    Optional<WardNewDTO> ward = wardService.findOneByCode(request.getWardCode());
                    if (ward.isPresent()) {
                        flag = true;
                        sb.append(ward.get().getInCountry()).append(", ");
                        sbEn.append(ward.get().getOutCountry()).append(", ");
                    }
                }
                if (!StringUtils.isEmpty(request.getDistrictCode())) {
                    Optional<DistrictDTO> district = districtService.findOne(request.getDistrictCode());
                    if (district.isPresent()) {
                        flag = true;
                        sb.append(district.get().getInCountry()).append(", ");
                        sbEn.append(district.get().getOutCountry()).append(", ");
                        CityDTO city = cityService.findOne(district.get().getCityId());
                        if (city != null) {
                            sb.append(city.getInCountry()).append(", ");
                            sbEn.append(city.getOutCountry()).append(", ");
                        }
                    }
                }
                if (flag) {
                    sb.append("Việt Nam");
                    sbEn.append("Viet Nam");
                }
            } else {
                boolean flag = false;
                if (!StringUtils.isEmpty(request.getAddress1())) {
                    flag = true;
                    sb.append(request.getAddress1()).append(", ");
                    sbEn.append(request.getAddress1()).append(", ");
                }
                if (!StringUtils.isEmpty(request.getAddress2())) {
                    flag = true;
                    sb.append(request.getAddress2()).append(", ");
                    sbEn.append(request.getAddress2()).append(", ");
                }
                if (!StringUtils.isEmpty(request.getCity())) {
                    flag = true;
                    sb.append(request.getCity()).append(", ");
                    sbEn.append(request.getCity()).append(", ");
                }
                if (!StringUtils.isEmpty(request.getLocationCode())) {
                    CitySimpleDTO cityDto = cityService.searchCityByCode(request.getLocationCode());
                    if (cityDto != null) {
                        flag = true;
                        sb.append(cityDto.getInCountry()).append(", ");
                        sbEn.append(cityDto.getOutCountry()).append(", ");
                    }
                }
                if (!StringUtils.isEmpty(request.getZipCode())) {
                    flag = true;
                    sb.append(request.getZipCode()).append(", ");
                    sbEn.append(request.getZipCode()).append(", ");
                }
                if (!StringUtils.isEmpty(request.getCountryCode())) {
                    CountryVM country = countryService.findOneByCode(request.getCountryCode(), false);
                    if (country != null) {
                        flag = true;
                        sb.append(country.getInCountry());
                        sbEn.append(country.getOutCountry());
                    }
                }
            }
        }
        rs.setVn(sb.toString());
        rs.setEn(sbEn.toString());
        return rs;
    }
}
