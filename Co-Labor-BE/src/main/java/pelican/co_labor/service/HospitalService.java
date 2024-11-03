package pelican.co_labor.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import pelican.co_labor.domain.hospital.Hospital;
import pelican.co_labor.repository.hospital.HospitalRepository;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Service
public class HospitalService {

    @Value("${public.data.api.key.decoded}")
    private String serviceKey;

    private final HospitalRepository hospitalRepository;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public HospitalService(HospitalRepository hospitalRepository, JdbcTemplate jdbcTemplate) {
        this.hospitalRepository = hospitalRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    // 전체 병원 데이터 가져오기
    public void fetchAndSaveHospitalData() {
        fetchAndSaveHospitalData(null);
    }

    // 지역 병원 데이터 가져오기
    public void fetchAndSaveHospitalData(String region) {
        String apiUrl = "http://apis.data.go.kr/B552657/HsptlAsembySearchService/getHsptlMdcncListInfoInqire";
        int numOfRows = 1000; // 한 번에 가져올 행 수
        int totalCount = getTotalCount(apiUrl, region); // 전체 데이터 개수
        int totalPages = (totalCount / numOfRows) + 1; // 전체 페이지 수

        try {
            // 데이터 삭제 및 테이블 초기화
            if (region == null) {
                hospitalRepository.deleteAll();
            } else {
                hospitalRepository.deleteByAddressStartingWith(region);
            }

            for (int pageNo = 1; pageNo <= totalPages; pageNo++) {
                String urlStr = apiUrl + "?serviceKey=" + serviceKey + "&numOfRows=" + numOfRows + "&pageNo=" + pageNo;
                if (region != null) {
                    urlStr += "&Q0=" + URLEncoder.encode(region, "UTF-8");
                }
                URL url = new URL(urlStr);
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(url.openStream());

                doc.getDocumentElement().normalize();

                NodeList nList = doc.getElementsByTagName("item");

                List<Hospital> hospitals = new ArrayList<>();

                for (int temp = 0; temp < nList.getLength(); temp++) {
                    Node nNode = nList.item(temp);

                    if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element eElement = (Element) nNode;

                        Hospital hospital = new Hospital();
                        hospital.setId(Long.parseLong(getTagValue("rnum", eElement)));
                        hospital.setAddress(getTagValue("dutyAddr", eElement));
                        hospital.setName(getTagValue("dutyName", eElement));
                        hospital.setPhone(getTagValue("dutyTel1", eElement));
                        hospital.setLatitude(parseDoubleOrDefault(getTagValue("wgs84Lat", eElement)));
                        hospital.setLongitude(parseDoubleOrDefault(getTagValue("wgs84Lon", eElement)));

                        hospitals.add(hospital);
                    }
                }

                hospitalRepository.saveAll(hospitals);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getTagValue(String tag, Element eElement) {
        NodeList nodeList = eElement.getElementsByTagName(tag);
        if (nodeList.getLength() > 0) {
            Node node = nodeList.item(0).getFirstChild();
            if (node != null) {
                return node.getNodeValue();
            }
        }
        return "";
    }

    private double parseDoubleOrDefault(String value) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    private int getTotalCount(String apiUrl, String region) {
        try {
            String urlStr = apiUrl + "?serviceKey=" + serviceKey + "&numOfRows=1&pageNo=1";
            if (region != null) {
                urlStr += "&Q0=" + URLEncoder.encode(region, "UTF-8");
            }
            URL url = new URL(urlStr);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(url.openStream());

            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("body");
            if (nList.getLength() > 0) {
                Element eElement = (Element) nList.item(0);
                return Integer.parseInt(getTagValue("totalCount", eElement));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<Hospital> getAllHospitals() {
        return hospitalRepository.findAll();
    }

    public List<Hospital> getHospitalsByRegion(String region) {
        return hospitalRepository.findByAddressStartingWith(region);
    }
}
