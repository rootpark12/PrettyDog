package com.pretty.dog.dao;

import java.util.ArrayList;
import java.util.HashMap;

import com.pretty.dog.dto.DogDTO;

public interface MembersDAO {

	int joinShs(HashMap<String, String> params);

	String overlayShsid(String id);

	int ShopjoinShs(HashMap<String, String> params);

	int ShopInfo(HashMap<String, String> params);

	int shopPhoto(String shopSaup, String oriFileName, String newFileName);

	String PassCk(String id, String pw);


	int joinShs(String id, String hashText, String name, String phone, String email);

	int ShopjoinShs(String id, String hashText, String name, String phone, String email);

	DogDTO MyjungboSujungshs(String id);

	DogDTO MyShopInfoshs(String id);

	int DogUp(String id, String dogname, String dogage, String dogweight, String dogchar);

	ArrayList<DogDTO> Mydogshs(String id);

	int DogDel(String id, String dogName);

	DogDTO MyDogsujungshs(String id, String dogName);

	int DogSujung(HashMap<String, String> params, String id);

	int DogSujung(String id, String dogname, String dogage, String dogweight, String dogchar);

	int memberOut(String id);

<<<<<<< HEAD
	int shopup(HashMap<String, String> params);
=======
	String shopSaupCk(String shopSaup);

	int userUp(HashMap<String, String> params);
>>>>>>> 0732ad51c5bcf30f510d8d59da40f61fda6016c0




}
