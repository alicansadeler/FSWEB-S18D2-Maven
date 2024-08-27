package com.workintech.sqldmlprocedures;

import com.workintech.sqldmlprocedures.entity.Kitap;
import com.workintech.sqldmlprocedures.entity.Ogrenci;
import com.workintech.sqldmlprocedures.entity.Tur;
import com.workintech.sqldmlprocedures.entity.Yazar;
import com.workintech.sqldmlprocedures.repository.KitapRepository;
import com.workintech.sqldmlprocedures.repository.OgrenciRepository;
import com.workintech.sqldmlprocedures.repository.TurRepository;
import com.workintech.sqldmlprocedures.repository.YazarRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(ResultAnalyzer.class)
class SqlDmlProceduresApplicationTests {
	private KitapRepository kitapRepository;
	private OgrenciRepository ogrenciRepository;
	private TurRepository turRepository;
	private YazarRepository yazarRepository;

	@Autowired
	public SqlDmlProceduresApplicationTests(KitapRepository kitapRepository, OgrenciRepository ogrenciRepository,
											TurRepository turRepository, YazarRepository yazarRepository) {
		this.kitapRepository = kitapRepository;
		this.ogrenciRepository = ogrenciRepository;
		this.turRepository = turRepository;
		this.yazarRepository = yazarRepository;
	}

	@DisplayName("Biyografi türünü tür tablosuna ekleyiniz.")
	@Test
	void findTurTest(){
		Tur tur = turRepository.findByAd("Biyografi");
		assertNotNull(tur);
		assertEquals(tur.getAd(), "Biyografi");
	}

	@DisplayName("Nurettin Belek isimli yazarı yazar tablosuna ekleyiniz.")
	@Test
	void findYazarTest(){
		Yazar yazar = yazarRepository.findByAd("Nurettin");
		assertNotNull(yazar);
		assertEquals(yazar.getAd(), "Nurettin");
		assertEquals(yazar.getSoyad(), "Belek");
	}

	@DisplayName("10B sınıfındaki öğrencileri 10C sınıfına geçirin.")
	@Test
	void findOgrenciBySinifTest(){
		List<Ogrenci> ogrenciList = ogrenciRepository.findBySinif("10C");
		assertNotNull(ogrenciList);
		assertEquals(ogrenciList.size(), 2);
	}

	@DisplayName("Tüm kitapların puanını 5 puan arttırınız.")
	@Test
	void findBookPointTest(){
		List<Kitap> kitapList = kitapRepository.findAll();
		assertEquals(kitapList.get(0).getPuan(), 27);
	}


    @DisplayName("Adı Mehmet olan tüm yazarları silin.")
	@Test
	void findNullYazarTest(){
		Yazar yazar = yazarRepository.findByAd("Mehmet");
		assertNull(yazar);
	}

	@DisplayName("Kişisel Gelişim isimli bir tür oluşturun.")
	@Test
	void findNewTurTest(){
		Tur tur = turRepository.findByAd("Kişisel Gelişim");
		assertNotNull(tur);
		assertEquals(tur.getAd(), "Kişisel Gelişim");
	}

	@DisplayName("'Benim Üniversitelerim' isimli kitabın türünü 'Kişisel Gelişim' yapın.")
	@Test
	void checkBookTurTest(){
		Tur tur = turRepository.findByAd("Kişisel Gelişim");
		Kitap kitap = kitapRepository.findByAd("Benim Üniversitelerim");
		assertEquals(tur.getTurno(), kitap.getTurno());
	}

	@DisplayName("Öğrenci tablosunu kontrol etmek amaçlı tüm öğrencileri görüntüleyen 'ogrencilistesi' adında bir fonksiyon oluşturun.")
	@Test
	void callOgrenciListesiTest(){
		List<Ogrenci> ogrenciList = ogrenciRepository.findAllOgrenci();
		assertNotNull(ogrenciList);
		assertEquals(ogrenciList.size(), 10);
	}

	@DisplayName("kitap tablosuna yeni kitap eklemek için 'ekle' adında bir prosedür oluşturun.")
	@Test
	void callEkleTest(){
		kitapRepository.addNewKitap("Alice in the Wonderland", 100, 1, 1);
		Kitap kitap = kitapRepository.findByAd("Alice in the Wonderland");
		assertNotNull(kitap);
		kitapRepository.delete(kitap);
	}

	@DisplayName("Öğrenci noya göre öğrenci silebilmeyi sağlayan 'sil' adında bir prosedür oluşturun.")
	@Test
	void removeOgrenciTest(){
		Ogrenci ogrenci = new Ogrenci();
	    ogrenci.setAd("Test");
		ogrenci.setSoyad("Test");
		ogrenci.setCinsiyet("E");
		ogrenci.setPuan(50);
		ogrenci.setSinif("10A");
		ogrenciRepository.save(ogrenci);

		List<Ogrenci> ogrenciList = ogrenciRepository.findAllOgrenci();
		Ogrenci ogrenci1 = ogrenciList.stream()
				.filter(ogr -> ogr.getAd().equals("Test")).collect(Collectors.toList()).get(0);

		ogrenciRepository.removeOgrenci(ogrenci1.getOgrno());

		ogrenciList = ogrenciRepository.findAllOgrenci();
		List<Ogrenci> testResult = ogrenciList.stream()
				.filter(ogr -> ogr.getAd().equals("Test")).collect(Collectors.toList());

		assertEquals(testResult.size(), 0);
	}
}
