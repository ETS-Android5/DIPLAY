package app.dinhcuong.diplay.Service;

public class APIService {
    private static String base_url = "https://ledhcg.000webhostapp.com/android_diplay/server/";
    public static app.dinhcuong.diplay.Service.DataService getService() {
        return app.dinhcuong.diplay.Service.APIRetrofitClient.getClient(base_url).create(app.dinhcuong.diplay.Service.DataService.class);
    }
}
