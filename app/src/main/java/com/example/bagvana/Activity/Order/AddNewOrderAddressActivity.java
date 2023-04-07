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
                    ReveiverInfoDAO reveiverInfoDAO = new ReveiverInfoDAO("1");
                    String newAddressID = Utils._newAddressID;

                    Log.e("activity", newAddressID);

                    ReceiverInfo receiverInfo = new ReceiverInfo("1", newAddressID, address, name, phone, isDefault);
                    reveiverInfoDAO.addReceiverInfoFirebase(receiverInfo);

                    Intent intent = new Intent(getApplicationContext(), OrderAddressActivity.class);
                    startActivity(intent);
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

                            if(districtID == R.id.spinner_district) {
                                switch (selectedDistrict) {
                                    case "Lựa chọn Quận hoặc Huyện":
                                        wardAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_default_ward, R.layout.spinner_layout);
                                        break;
                                    case "TP. Long Xuyên":
                                        wardAdapter = ArrayAdapter.createFromResource(adapterView.getContext(), R.array.array_long_xuyen_ward_an_giang_district, R.layout.spinner_layout);
                                    default:
                                        break;
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