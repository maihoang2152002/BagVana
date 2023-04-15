package com.example.bagvana.Utils;

import com.example.bagvana.DTO.Product;
import com.example.bagvana.DTO.ReceiverInfo;
import com.example.bagvana.DTO.ReceiverInfo;
import com.example.bagvana.DTO.User;
import com.example.bagvana.DTO.Voucher;
import com.example.bagvana.DTO.Voucher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Utils {

    // Order
    public static ArrayList<Product> _productList = new ArrayList<>(); // sản phẩm chọn mua
    public static ReceiverInfo _receiverInfo = new ReceiverInfo(); // địa chỉ mua hàng (để mặc định cho đến khi khách hàng thay đổi)
    public static String _newAddressID; // id dể tạo địa chỉ mới
    public static ArrayList<Voucher> _voucherList = new ArrayList<>(); // voucher được áp dụng cho đơn hàng
    public static Product _product_current = new Product();
    public static Product _new_product = new Product();
    public static HashMap<String, Integer> _vouchersOfUser = new HashMap<>();
    public static User _user = new User();

    public static List<User> _list_user = new ArrayList<>();


}
