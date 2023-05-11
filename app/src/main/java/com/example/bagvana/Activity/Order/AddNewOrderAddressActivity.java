package com.example.bagvana.Activity.Order;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.bagvana.DAO.ReveiverInfoDAO;
import com.example.bagvana.DTO.ReceiverInfo;
import com.example.bagvana.R;
import com.example.bagvana.Utils.Utils;

public class AddNewOrderAddressActivity extends AppCompatActivity {

    private String selectedCity, selectedDistrict, selectedWard;
    private TextView txt_fullName, txt_phone, txt_city, txt_district, txt_ward, txt_street;
    private Spinner spinner_city, spinner_district, spinner_ward;
    private ArrayAdapter<CharSequence> cityAdapter, districtAdapter, wardAdapter;
    private EditText editTxt_fullName;
    private EditText editTxt_phone;
    private EditText editTxt_street;
    private CheckBox checkBox_default;
    private Button btn_complete;
    private Toolbar toolbar_add_new_address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_order_address);

        toolbar_add_new_address = findViewById(R.id.toolbar_add_new_address);
        setSupportActionBar(toolbar_add_new_address);

        txt_fullName = findViewById(R.id.txt_fullName);
        txt_phone = findViewById(R.id.txt_phone);
        txt_city = findViewById(R.id.txt_city);
        txt_district = findViewById(R.id.txt_district);
        txt_ward = findViewById(R.id.txt_ward);
        txt_street = findViewById(R.id.txt_street);

        editTxt_fullName = findViewById(R.id.editTxt_fullName);
        editTxt_phone = findViewById(R.id.editTxt_phone);

        spinner_city = findViewById(R.id.spinner_city);
        spinner_district = findViewById(R.id.spinner_district);
        spinner_ward = findViewById(R.id.spinner_ward);
        editTxt_street = findViewById(R.id.editTxt_street);

        checkBox_default = findViewById(R.id.checkBox_default);

        btn_complete = findViewById(R.id.btn_complete);


        initLocationInVietNam();

        btn_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name, phone, address;
                boolean isDefault = false;
                boolean success = true;

                name = editTxt_fullName.getText().toString();
                phone = editTxt_phone.getText().toString();
                address = editTxt_street.getText().toString() + ", " +  selectedWard + ", " + selectedDistrict + ", " + selectedCity;

                if(checkBox_default.isChecked()) {
                    isDefault = true;
                }

                if(name.equals("")) {
                    txt_fullName.setError("Bắt buộc");
                    txt_fullName.requestFocus();

                    success = false;
                } else {
                    txt_fullName.setError(null);
                }

                if(phone.equals("")) {
                    txt_phone.setError("Bắt buộc");
                    txt_phone.requestFocus();

                    success = false;
                }else {
                    txt_phone.setError(null);
                }

                if(selectedCity.equals("Lựa chọn Tỉnh hoặc Thành Phố")) {
                    txt_city.setError("Bắt buộc");
                    txt_city.requestFocus();

                    success = false;
                }else {
                    txt_city.setError(null);
                }

                if(selectedDistrict.equals("Lựa chọn Quận hoặc Huyện")) {
                    txt_district.setError("Bắt buộc");
                    txt_district.requestFocus();

                    success = false;
                }else {
                    txt_district.setError(null);
                }

                if(selectedWard.equals("Lựa chọn Phường hoặc Xã")) {
                    txt_ward.setError("Bắt buộc");
                    txt_ward.requestFocus();

                    success = false;
                }else {
                    txt_ward.setError(null);
                }

                if(editTxt_street.getText().toString().equals("")) {
                    txt_street.setError("Bắt buộc");
                    txt_street.requestFocus();

                    success = false;
                }else {
                    txt_street.setError(null);
                }

                if(success) {
                    ReveiverInfoDAO reveiverInfoDAO = new ReveiverInfoDAO();
                    String newAddressID = Utils._newAddressID;

                    //kiểm tra default

                    ReceiverInfo receiverInfo = new ReceiverInfo(Utils._user.getId(), newAddressID, address, name, phone, isDefault);

                    reveiverInfoDAO.addReceiverInfoFirebase(receiverInfo);

                    finish();
                }
            }
        });
    }

    private void initLocationInVietNam() {
        cityAdapter = ArrayAdapter.createFromResource(this, R.array.array_vietnam_city, R.layout.spinner_layout);

        cityAdapter.setDropDownViewResource(com.google.android.material.R.layout.support_simple_spinner_dropdown_item);

        spinner_city.setAdapter(cityAdapter);
        spinner_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                selectedCity = spinner_city.getSelectedItem().toString();

                int cityID = adapterView.getId();

                if(cityID == R.id.spinner_city) {
                    switch (selectedCity) {
                        case "Lựa chọn Tỉnh hoặc Thành Phố":
                            districtAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_default_district, R.layout.spinner_layout);
                            break;
                        case "An Giang":
                            districtAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_an_giang_district, R.layout.spinner_layout);
                            break;
                        case "Bà Rịa-Vũng Tàu":
                            districtAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_ba_ria_vung_tau_district, R.layout.spinner_layout);
                            break;
                        case "Bắc Giang":
                            districtAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_bac_giang_district, R.layout.spinner_layout);
                            break;
                        case "Bắc Kạn":
                            districtAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_bac_kan_district, R.layout.spinner_layout);
                            break;
                        case "Bạc Liêu":
                            districtAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_bac_lieu_district, R.layout.spinner_layout);
                            break;
                        case "Bắc Ninh":
                            districtAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_bac_ninh_district, R.layout.spinner_layout);
                            break;
                        case "Bến Tre":
                            districtAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_ben_tre_district, R.layout.spinner_layout);
                            break;
                        case "Bình Định":
                            districtAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_binh_dinh_district, R.layout.spinner_layout);
                            break;
                        case "Bình Dương":
                            districtAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_binh_duong_district, R.layout.spinner_layout);
                            break;
                        case "Bình Phước":
                            districtAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_binh_phuoc_district, R.layout.spinner_layout);
                            break;
                        case "Bình Thuận":
                            districtAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_binh_thuan_district, R.layout.spinner_layout);
                            break;
                        case "Cà Mau":
                            districtAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_ca_mau_district, R.layout.spinner_layout);
                            break;
                        case "Cần Thơ":
                            districtAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_can_tho_district, R.layout.spinner_layout);
                            break;
                        case "Cao Bằng":
                            districtAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_cao_bang_district, R.layout.spinner_layout);
                            break;
                        case "Đà Nẵng":
                            districtAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_da_nang_district, R.layout.spinner_layout);
                            break;
                        case "Đắk Lắk":
                            districtAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_dak_lak_district, R.layout.spinner_layout);
                            break;
                        case "Đắk Nông":
                            districtAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_dak_nong_district, R.layout.spinner_layout);
                            break;
                        case "Điện Biên":
                            districtAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_dien_bien_district, R.layout.spinner_layout);
                            break;
                        case "Đồng Nai":
                            districtAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_dong_nai_district, R.layout.spinner_layout);
                            break;
                        case "Đồng Tháp":
                            districtAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_dong_thap_district, R.layout.spinner_layout);
                            break;
                        case "Gia Lai":
                            districtAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_gia_lai_district, R.layout.spinner_layout);
                            break;
                        case "Hà Giang":
                            districtAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_ha_giang_district, R.layout.spinner_layout);
                            break;
                        case "Hà Nam":
                            districtAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_ha_nam_district, R.layout.spinner_layout);
                            break;
                        case "Hà Nội":
                            districtAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_ha_noi_district, R.layout.spinner_layout);
                            break;
                        case "Hà Tĩnh":
                            districtAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_ha_tinh_district, R.layout.spinner_layout);
                            break;
                        case "Hải Dương":
                            districtAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_hai_duong_district, R.layout.spinner_layout);
                            break;
                        case "Hải Phòng":
                            districtAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_hai_phong_district, R.layout.spinner_layout);
                            break;
                        case "Hậu Giang":
                            districtAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_hau_giang_district, R.layout.spinner_layout);
                            break;
                        case "Hòa Bình":
                            districtAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_hoa_binh_district, R.layout.spinner_layout);
                            break;
                        case "Hưng Yên":
                            districtAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_hung_yen_district, R.layout.spinner_layout);
                            break;
                        case "Khánh Hòa":
                            districtAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_khanh_hoa_district, R.layout.spinner_layout);
                            break;
                        case "Kiên Giang":
                            districtAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_kien_giang_district, R.layout.spinner_layout);
                            break;
                        case "Kon Tum":
                            districtAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_kon_tum_district, R.layout.spinner_layout);
                            break;
                        case "Lai Châu":
                            districtAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_lai_chau_district, R.layout.spinner_layout);
                            break;
                        case "Lâm Đồng":
                            districtAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_lam_dong_district, R.layout.spinner_layout);
                            break;
                        case "Lạng Sơn":
                            districtAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_lang_son_district, R.layout.spinner_layout);
                            break;
                        case "Lào Cai":
                            districtAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_lao_cai_district, R.layout.spinner_layout);
                            break;
                        case "Long An":
                            districtAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_long_an_district, R.layout.spinner_layout);
                            break;
                        case "Nam Định":
                            districtAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_nam_dinh_district, R.layout.spinner_layout);
                            break;
                        case "Nghệ An":
                            districtAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_nghe_an_district, R.layout.spinner_layout);
                            break;
                        case "Ninh Bình":
                            districtAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_ninh_binh_district, R.layout.spinner_layout);
                            break;
                        case "Ninh Thuận":
                            districtAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_ninh_thuan_district, R.layout.spinner_layout);
                            break;
                        case "Phú Thọ":
                            districtAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_phu_tho_district, R.layout.spinner_layout);
                            break;
                        case "Phú Yên":
                            districtAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_phu_yen_district, R.layout.spinner_layout);
                            break;
                        case "Quảng Bình":
                            districtAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_quang_binh_district, R.layout.spinner_layout);
                            break;
                        case "Quảng Nam":
                            districtAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_quang_nam_district, R.layout.spinner_layout);
                            break;
                        case "Quảng Ngãi":
                            districtAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_quang_ngai_district, R.layout.spinner_layout);
                            break;
                        case "Quảng Ninh":
                            districtAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_quang_ninh_district, R.layout.spinner_layout);
                            break;
                        case "Quảng Trị":
                            districtAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_quang_tri_district, R.layout.spinner_layout);
                            break;
                        case "Sóc Trăng":
                            districtAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_soc_trang_district, R.layout.spinner_layout);
                            break;
                        case "Sơn La":
                            districtAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_son_la_district, R.layout.spinner_layout);
                            break;
                        case "Tây Ninh":
                            districtAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_tay_ninh_district, R.layout.spinner_layout);
                            break;
                        case "Thái Bình":
                            districtAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_thai_binh_district, R.layout.spinner_layout);
                            break;
                        case "Thái Nguyên":
                            districtAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_thai_nguyen_district, R.layout.spinner_layout);
                            break;
                        case "Thanh Hóa":
                            districtAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_thanh_hoa_district, R.layout.spinner_layout);
                            break;
                        case "Thừa Thiên Huế":
                            districtAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_thua_thien_hue_district, R.layout.spinner_layout);
                            break;
                        case "Tiền Giang":
                            districtAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_tien_giang_district, R.layout.spinner_layout);
                            break;
                        case "TP. Hồ Chí Minh":
                            districtAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_ho_chi_minh_district, R.layout.spinner_layout);
                            break;
                        case "Trà Vinh":
                            districtAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_tra_vinh_district, R.layout.spinner_layout);
                            break;
                        case "Tuyên Quang":
                            districtAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_tuyen_quang_district, R.layout.spinner_layout);
                            break;
                        case "Vĩnh Long":
                            districtAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_vinh_long_district, R.layout.spinner_layout);
                            break;
                        case "Vĩnh Phúc":
                            districtAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_vinh_phuc_district, R.layout.spinner_layout);
                            break;
                        case "Yên Bái":
                            districtAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_yen_bai_district, R.layout.spinner_layout);
                            break;
                        default:
                            break;
                    }

                    districtAdapter.setDropDownViewResource(com.google.android.material.R.layout.support_simple_spinner_dropdown_item);

                    spinner_district.setAdapter(districtAdapter);

                    spinner_district.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            selectedDistrict = spinner_district.getSelectedItem().toString();

                            int districtID = adapterView.getId();
                            Log.e("city", selectedCity);

                            if(districtID == R.id.spinner_district) {
                                if (selectedDistrict.equals("Lựa chọn Quận hoặc Huyện")) {
                                    wardAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_default_ward, R.layout.spinner_layout);
                                }
                                if (selectedCity.equals("An Giang")) {
                                    switch (selectedDistrict) {
                                        case "TP. Long Xuyên":
                                            wardAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_long_xuyen_ward_an_giang_district, R.layout.spinner_layout);
                                            break;
                                        case "TP. Châu Đốc":
                                            wardAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_chau_doc_ward_an_giang_district, R.layout.spinner_layout);
                                            break;
                                        case "Thị xã Tân Châu":
                                            wardAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_tan_chau_ward_an_giang_district, R.layout.spinner_layout);
                                            break;
                                        case "Huyện An Phú":
                                            wardAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_an_phu_ward_an_giang_district, R.layout.spinner_layout);
                                            break;
                                        case "Huyện Châu Phú":
                                            wardAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_chau_phu_ward_an_giang_district, R.layout.spinner_layout);
                                            break;
                                        case "Huyện Châu Thành":
                                            wardAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_chau_thanh_ward_an_giang_district, R.layout.spinner_layout);
                                            break;
                                        case "Huyện Chợ Mới":
                                            wardAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_cho_moi_ward_an_giang_district, R.layout.spinner_layout);
                                            break;
                                        case "Huyện Phú Tân":
                                            wardAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_phu_tan_ward_an_giang_district, R.layout.spinner_layout);
                                            break;
                                        case "Huyện Thoại Sơn":
                                            wardAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_thoai_son_ward_an_giang_district, R.layout.spinner_layout);
                                            break;
                                        case "Huyện Tịnh Biên":
                                            wardAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_tinh_bien_ward_an_giang_district, R.layout.spinner_layout);
                                            break;
                                        case "Huyện Tri Tôn":
                                            wardAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_tri_ton_ward_an_giang_district, R.layout.spinner_layout);
                                            break;
                                        default:
                                            break;
                                    }
                                }

                                if (selectedCity.equals("Bà Rịa-Vũng Tàu")) {
                                    switch (selectedDistrict) {
                                        case "TP. Bà Rịa":
                                            wardAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_ba_ria_ward_ba_ria_vung_tau_district, R.layout.spinner_layout);
                                            break;
                                        case "TP. Vũng Tàu":
                                            wardAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_vung_tau_ward_ba_ria_vung_tau_district, R.layout.spinner_layout);
                                            break;
                                        case "Thị xã Châu Đức":
                                            wardAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_chau_duc_ward_ba_ria_vung_tau_district, R.layout.spinner_layout);
                                            break;
                                        case "Thị xã Côn Đảo":
                                            wardAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_con_dao_ward_ba_ria_vung_tau_district, R.layout.spinner_layout);
                                            break;
                                        case "Thị xã Đất Đỏ":
                                            wardAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_dat_do_ward_ba_ria_vung_tau_district, R.layout.spinner_layout);
                                            break;
                                        case "Huyện Long Điền":
                                            wardAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_long_dien_ward_ba_ria_vung_tau_district, R.layout.spinner_layout);
                                            break;
                                        case "Huyện Tân Thành":
                                            wardAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_tan_thanh_ward_ba_ria_vung_tau_district, R.layout.spinner_layout);
                                            break;
                                        case "Huyện Xuyên Mộc":
                                            wardAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_xuyen_moc_ward_ba_ria_vung_tau_district, R.layout.spinner_layout);
                                            break;
                                        default:
                                            break;
                                    }
                                }

                                if (selectedCity.equals("Bạc Liêu")) {
                                    switch (selectedDistrict) {
                                        case "Thành phố Bạc Liêu":
                                            wardAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_bac_lieu_ward_bac_lieu_district, R.layout.spinner_layout);
                                            break;
                                        case "Thị xã Giá Rai":
                                            wardAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_gia_rai_ward_bac_lieu_district, R.layout.spinner_layout);
                                            break;
                                        case "Huyện Đông Hải":
                                            wardAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_dong_hai_ward_bac_lieu_district, R.layout.spinner_layout);
                                            break;
                                        case "Huyện Hòa Bình":
                                            wardAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_hoa_binh_ward_bac_lieu_district, R.layout.spinner_layout);
                                            break;
                                        case "Huyện Hồng Dân":
                                            wardAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_hong_dan_ward_bac_lieu_district, R.layout.spinner_layout);
                                            break;
                                        case "Huyện Phước Long":
                                            wardAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_phuoc_long_ward_bac_lieu_district, R.layout.spinner_layout);
                                            break;
                                        case "Huyện Vĩnh Lợi":
                                            wardAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_vinh_loi_ward_bac_lieu_district, R.layout.spinner_layout);
                                            break;
                                        default:
                                            break;
                                    }
                                }

                                if (selectedCity.equals("Bắc Kạn")) {
                                    switch (selectedDistrict) {
                                        case "TP. Bắc Kạn":
                                            wardAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_bac_kan_ward_bac_kan_district, R.layout.spinner_layout);
                                            break;
                                        case "Thị xã Ba Bể":
                                            wardAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_ba_be_ward_bac_kan_district, R.layout.spinner_layout);
                                            break;
                                        case "Huyện Bạch Thông":
                                            wardAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_bach_thong_ward_bac_kan_district, R.layout.spinner_layout);
                                            break;
                                        case "Huyện Chợ Đồn":
                                            wardAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_cho_don_ward_bac_kan_district, R.layout.spinner_layout);
                                            break;
                                        case "Huyện Na Rì":
                                            wardAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_na_ri_ward_bac_kan_district, R.layout.spinner_layout);
                                            break;
                                        case "Huyện Ngân Sơn":
                                            wardAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_ngan_son_ward_bac_kan_district, R.layout.spinner_layout);
                                            break;
                                        case "Huyện Pác Nặm":
                                            wardAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_pac_nam_ward_bac_kan_district, R.layout.spinner_layout);
                                            break;
                                        default:
                                            break;
                                    }
                                }

                                if (selectedCity.equals("Bắc Giang")) {
                                    switch (selectedDistrict) {
                                        case "TP. Bắc Giang":
                                            wardAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_bac_giang_ward_bac_giang_district, R.layout.spinner_layout);
                                            break;

                                        case "Thị xã Yên Dũng":
                                            wardAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_yen_dung_ward_bac_giang_district, R.layout.spinner_layout);
                                            break;
                                        case "Thị xã Lạng Giang":
                                            wardAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_lang_giang_ward_bac_giang_district, R.layout.spinner_layout);
                                            break;
                                        case "Huyện Hiệp Hoà":
                                            wardAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_hiep_hoa_ward_bac_giang_district, R.layout.spinner_layout);
                                            break;
                                        case "Huyện Lục Nam":
                                            wardAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_luc_nam_ward_bac_giang_district, R.layout.spinner_layout);
                                            break;
                                        case "Huyện Lục Ngạn":
                                            wardAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_luc_ngan_ward_bac_giang_district, R.layout.spinner_layout);
                                            break;
                                        case "Huyện Sơn Động":
                                            wardAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_son_dong_ward_bac_giang_district, R.layout.spinner_layout);
                                            break;
                                        case "Huyện Tân Yên":
                                            wardAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_tan_yen_ward_bac_giang_district, R.layout.spinner_layout);
                                            break;
                                        case "Huyện Việt Yên":
                                            wardAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_viet_yen_ward_bac_giang_district, R.layout.spinner_layout);
                                            break;
                                        case "Huyện Hiệp Sơn":
                                            wardAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_hiep_son_ward_bac_giang_district, R.layout.spinner_layout);
                                            break;
                                        default:
                                            break;
                                    }
                                }

                                if (selectedCity.equals("Bắc Ninh")) {
                                    switch (selectedDistrict) {
                                        case "Thành phố Bắc Ninh":
                                            wardAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_bac_ninh_ward_bac_ninh_district, R.layout.spinner_layout);
                                            break;

                                        case "Thị xã Từ Sơn":
                                            wardAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_tu_son_ward_bac_ninh_district, R.layout.spinner_layout);
                                            break;
                                        case "Huyện Ân Thi":
                                            wardAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_an_thi_ward_bac_ninh_district, R.layout.spinner_layout);
                                            break;
                                        case "Huyện Gia Bình":
                                            wardAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_gia_binh_ward_bac_ninh_district, R.layout.spinner_layout);
                                            break;
                                        case "Huyện Quế Võ":
                                            wardAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_que_vo_ward_bac_ninh_district, R.layout.spinner_layout);
                                            break;
                                        case "Huyện Tiên Du":
                                            wardAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_tien_du_ward_bac_ninh_district, R.layout.spinner_layout);
                                            break;
                                        case "Huyện Yên Phong":
                                            wardAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_yen_phong_ward_bac_ninh_district, R.layout.spinner_layout);
                                            break;
                                        default:
                                            break;
                                    }
                                }

                                if (selectedCity.equals("Bến Tre")) {
                                    switch (selectedDistrict) {
                                        case "Thành phố Bến Tre":
                                            wardAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_ben_tre_ward_ben_tre_district, R.layout.spinner_layout);
                                            break;
                                        case "Thị xã Ba Tri":
                                            wardAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_ba_tri_ward_ben_tre_district, R.layout.spinner_layout);
                                            break;
                                        case "Huyện Châu Thành":
                                            wardAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_chau_thanh_ward_ben_tre_district, R.layout.spinner_layout);
                                            break;
                                        case "Huyện Chợ Lách":
                                            wardAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_cho_lach_ward_ben_tre_district, R.layout.spinner_layout);
                                            break;
                                        case "Huyện Giồng Trôm":
                                            wardAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_giong_trom_ward_ben_tre_district, R.layout.spinner_layout);
                                            break;
                                        case "Huyện Mỏ Cày Bắc":
                                            wardAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_mo_cay_bac_ward_ben_tre_district, R.layout.spinner_layout);
                                            break;
                                        case "Huyện Mỏ Cày Nam":
                                            wardAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_mo_cay_nam_ward_ben_tre_district, R.layout.spinner_layout);
                                            break;
                                        case "Huyện Thạnh Phú":
                                            wardAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_thach_phu_ward_ben_tre_district, R.layout.spinner_layout);
                                            break;
                                        default:
                                            break;
                                    }
                                }

                                if (selectedCity.equals("Bình Dương")) {
                                    switch (selectedDistrict) {
                                        case "TP. Bến Cát":
                                            wardAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_ben_cat_ward_binh_duong_district, R.layout.spinner_layout);
                                            break;
                                        case "TP. Dĩ An":
                                            wardAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_di_an_ward_binh_duong_district, R.layout.spinner_layout);
                                            break;
                                        case "TP. Tân Uyên":
                                            wardAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_tan_uyen_ward_binh_duong_district, R.layout.spinner_layout);
                                            break;
                                        case "TP. Thuận An":
                                            wardAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_thuan_an_ward_binh_duong_district, R.layout.spinner_layout);
                                            break;
                                        case "Thị xã Bàu Bàng":
                                            wardAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_bau_bang_ward_binh_duong_district, R.layout.spinner_layout);
                                            break;
//                                        case "Thị xã Phú Giáo":
//                                            wardAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_mo_cay_bac_ward_ben_tre_district, R.layout.spinner_layout);
//                                            break;
//                                        case "Huyện Đại Từ":
//                                            wardAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_mo_cay_nam_ward_ben_tre_district, R.layout.spinner_layout);
//                                            break;
//                                        case "Huyện Định Quán":
//                                            wardAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_thach_phu_ward_ben_tre_district, R.layout.spinner_layout);
//                                            break;
//                                        case "Huyện Tân Thành":
//                                            wardAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_thach_phu_ward_ben_tre_district, R.layout.spinner_layout);
//                                            break;
                                        default:
                                            break;
                                    }
                                }

                                if (selectedCity.equals("TP. Hồ Chí Minh")) {
                                    switch (selectedDistrict) {
                                        case "TP. Thủ Đức":
                                            wardAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_thu_duc_ward_ho_chi_minh_district, R.layout.spinner_layout);
                                            break;
                                        case "Quận 1":
                                            wardAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_quan_1_ward_ho_chi_minh_district, R.layout.spinner_layout);
                                            break;
                                        case "Quận 2":
                                            wardAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_quan_2_ward_ho_chi_minh_district, R.layout.spinner_layout);
                                            break;
                                        case "Quận 3":
                                            wardAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_quan_3_ward_ho_chi_minh_district, R.layout.spinner_layout);
                                            break;
                                        case "Quận 4":
                                            wardAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_quan_4_ward_ho_chi_minh_district, R.layout.spinner_layout);
                                            break;
                                        case "Quận 5":
                                            wardAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_quan_5_ward_ho_chi_minh_district, R.layout.spinner_layout);
                                            break;
                                        case "Quận 6":
                                            wardAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_quan_6_ward_ho_chi_minh_district, R.layout.spinner_layout);
                                            break;
                                        case "Quận 7":
                                            wardAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_quan_7_ward_ho_chi_minh_district, R.layout.spinner_layout);
                                            break;
                                        case "Quận 8":
                                            wardAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_quan_8_ward_ho_chi_minh_district, R.layout.spinner_layout);
                                            break;
                                        case "Quận 9":
                                            wardAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_quan_9_ward_ho_chi_minh_district, R.layout.spinner_layout);
                                            break;
                                        case "Quận 10":
                                            wardAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_quan_10_ward_ho_chi_minh_district, R.layout.spinner_layout);
                                            break;
                                        case "Quận 11":
                                            wardAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_quan_11_ward_ho_chi_minh_district, R.layout.spinner_layout);
                                            break;
                                        case "Quận 12":
                                            wardAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_quan_12_ward_ho_chi_minh_district, R.layout.spinner_layout);
                                            break;
                                        case "Quận Bình Tân":
                                            wardAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_binh_tan_ward_ho_chi_minh_district, R.layout.spinner_layout);
                                            break;
                                        case "Quận Bình Thạnh":
                                            wardAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_binh_thanh_ward_ho_chi_minh_district, R.layout.spinner_layout);
                                            break;
                                        case "Quận Gò Vấp":
                                            wardAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_go_vap_ward_ho_chi_minh_district, R.layout.spinner_layout);
                                            break;
                                        case "Quận Phú Nhuận":
                                            wardAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_phu_nhuan_ward_ho_chi_minh_district, R.layout.spinner_layout);
                                            break;
                                        case "Quận Tân Bình":
                                            wardAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_tan_binh_ward_ho_chi_minh_district, R.layout.spinner_layout);
                                            break;
                                        case "Quận Tân Phú":
                                            wardAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_tan_phu_ward_ho_chi_minh_district, R.layout.spinner_layout);
                                            break;
                                        case "Huyện Bình Chánh":
                                            wardAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_binh_chanh_ward_ho_chi_minh_district, R.layout.spinner_layout);
                                            break;
                                        case "Huyện Cần Giờ":
                                            wardAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_can_gio_ward_ho_chi_minh_district, R.layout.spinner_layout);
                                            break;
                                        case "Huyện Củ Chi":
                                            wardAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_cu_chi_ward_ho_chi_minh_district, R.layout.spinner_layout);
                                            break;
                                        case "Huyện Hóc Môn":
                                            wardAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_hoc_mon_ward_ho_chi_minh_district, R.layout.spinner_layout);
                                            break;
                                        case "Huyện Nhà Bè":
                                            wardAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_nha_be_ward_ho_chi_minh_district, R.layout.spinner_layout);
                                            break;
                                        default:
                                            break;
                                    }
                                }
                            }

                                wardAdapter.setDropDownViewResource(com.google.android.material.R.layout.support_simple_spinner_dropdown_item);

                                spinner_ward.setAdapter(wardAdapter);

                                spinner_ward.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                        selectedWard = spinner_ward.getSelectedItem().toString();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> adapterView) {

                                    }
                                });
                            }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setSupportActionBar(Toolbar toolbar_add_new_address) {
        toolbar_add_new_address.setNavigationIcon(R.drawable.ic_back);
        toolbar_add_new_address.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


}