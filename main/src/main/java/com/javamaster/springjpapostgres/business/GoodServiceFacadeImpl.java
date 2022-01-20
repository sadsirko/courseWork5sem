//package com.javamaster.springjpapostgres.business;
//
//import com.javamaster.springjpapostgres.business.service.GoodService;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//import java.util.stream.Collectors;
//import java.util.stream.Stream;
//
//@Component
//public class GoodServiceFacadeImpl implements GoodServiceFacade {
//
//    private final GoodService goodService;
//    private final GoodCashService goodCashService;
//    private final ProviderService providerService;
//    private final CashLogService cashLogService;
//
//    public GoodServiceFacadeImpl(GoodService goodService, GoodCashService goodCashService,
//                                 ProviderService providerService, CashLogService cashLogService) {
//        this.goodService = goodService;
//        this.goodCashService = goodCashService;
//        this.providerService = providerService;
//        this.cashLogService = cashLogService;
//    }
//
//    @Override
//    public List<Good> search(String query) {
//        return goodService.getSearch(query);
//    }
//
//    @Override
//    public void create(Good good) {
//        goodService.save(good);
//    }
//
//    @Override
//    public List<Good> read() {
//        return goodService.findAll();
//    }
//
//    @Override
//    public void update(Good good) {
//        goodService.update(good);
//    }
//
//    @Override
//    public void delete(Long id) {
//        goodService.delete(id);
//    }
//
//
//
//    @Override
//    public void cashData(){
//        List<Good> provider1Data = null;
//        List<Good> provider2Data = null;
//        try {
//            provider1Data = providerService.getProvider1All().get();
//            provider2Data = providerService.getProvider2All().get();
//        }catch (Exception e) {
//            e.printStackTrace();
//        }
//        goodCashService.deleteAll();
//        goodCashService.saveAll(provider1Data);
//        goodCashService.saveAll(provider2Data);
//    }
//
//    @Override
//    public List<Good> getFullSearch(String query) {
//        if(cashLogService.isCooldownGone()){
//            cashData();
//        }
//        List<Good> providers = goodCashService.toGoodList(goodCashService.getSearch(query));
//        List<Good> main = goodService.getSearch(query);
//        List<Good> result  = Stream.concat(main.stream(), providers.stream())
//                .collect(Collectors.toList());
//        return result;
//    }
//
//    @Override
//    public List<Good> getFullAll() {
//        if(cashLogService.isCooldownGone()){
//            cashData();
//        }
//        List<Good> providers = goodCashService.toGoodList(goodCashService.getSearch(""));
//        List<Good> main = goodService.getSearch("");
//        List<Good> result  = Stream.concat(main.stream(), providers.stream())
//                .collect(Collectors.toList());
//        return result;
//
//    }
//}
