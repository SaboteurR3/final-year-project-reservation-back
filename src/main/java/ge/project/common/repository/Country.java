package ge.project.common.repository;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Country {
    AFGHANISTAN("Afghanistan"),
    ALBANIA("Albania"),
    ALGERIA("Algeria"),
    ANDORRA("Andorra"),
    ANGOLA("Angola"),
    ARGENTINA("Argentina"),
    ARMENIA("Armenia"),
    AUSTRALIA("Australia"),
    AUSTRIA("Austria"),
    AZERBAIJAN("Azerbaijan"),
    BAHAMAS("Bahamas"),
    BAHRAIN("Bahrain"),
    BANGLADESH("Bangladesh"),
    BARBADOS("Barbados"),
    BELARUS("Belarus"),
    BELGIUM("Belgium"),
    BELIZE("Belize"),
    BENIN("Benin"),
    BHUTAN("Bhutan"),
    BOLIVIA("Bolivia"),
    BOSNIA_AND_HERZEGOVINA("Bosnia and Herzegovina"),
    BOTSWANA("Botswana"),
    BRAZIL("Brazil"),
    BRUNEI("Brunei"),
    BULGARIA("Bulgaria"),
    BURKINA_FASO("Burkina Faso"),
    BURUNDI("Burundi"),
    CABO_VERDE("Cabo Verde"),
    CAMBODIA("Cambodia"),
    CAMEROON("Cameroon"),
    CANADA("Canada"),
    CENTRAL_AFRICAN_REPUBLIC("Central African Republic"),
    CHAD("Chad"),
    CHILE("Chile"),
    CHINA("China"),
    COLOMBIA("Colombia"),
    COMOROS("Comoros"),
    CONGO("Congo"),
    DRC("DRC"),
    COSTA_RICA("Costa Rica"),
    COTE_D_IVOIRE("Cote d'Ivoire"),
    CROATIA("Croatia"),
    CUBA("Cuba"),
    CYPRUS("Cyprus"),
    CZECH_REPUBLIC("Czech Republic"),
    DENMARK("Denmark"),
    DJIBOUTI("Djibouti"),
    DOMINICAN_REPUBLIC("Dominican Republic"),
    ECUADOR("Ecuador"),
    EGYPT("Egypt"),
    EL_SALVADOR("El Salvador"),
    EQUATORIAL_GUINEA("Equatorial Guinea"),
    ERITREA("Eritrea"),
    ESTONIA("Estonia"),
    ESWATINI("Eswatini"),
    ETHIOPIA("Ethiopia"),
    FIJI("Fiji"),
    FINLAND("Finland"),
    FRANCE("France"),
    GABON("Gabon"),
    GAMBIA("Gambia"),
    GEORGIA("Georgia"),
    GERMANY("Germany"),
    GHANA("Ghana"),
    GREECE("Greece"),
    GUATEMALA("Guatemala"),
    GUINEA("Guinea"),
    GUINEA_BISSAU("Guinea-Bissau"),
    GUYANA("Guyana"),
    HAITI("Haiti"),
    HONDURAS("Honduras"),
    HUNGARY("Hungary"),
    ICELAND("Iceland"),
    INDIA("India"),
    INDONESIA("Indonesia"),
    IRAN("Iran"),
    IRAQ("Iraq"),
    IRELAND("Ireland"),
    ISRAEL("Israel"),
    ITALY("Italy"),
    JAMAICA("Jamaica"),
    JAPAN("Japan"),
    JORDAN("Jordan"),
    KAZAKHSTAN("Kazakhstan"),
    KENYA("Kenya"),
    KOSOVO("Kosovo"),
    KUWAIT("Kuwait"),
    KYRGYZSTAN("Kyrgyzstan"),
    LAOS("Laos"),
    LATVIA("Latvia"),
    LEBANON("Lebanon"),
    LESOTHO("Lesotho"),
    LIBERIA("Liberia"),
    LIBYA("Libya"),
    LITHUANIA("Lithuania"),
    LUXEMBOURG("Luxembourg"),
    MADAGASCAR("Madagascar"),
    MALAWI("Malawi"),
    MALAYSIA("Malaysia"),
    MALDIVES("Maldives"),
    MALI("Mali"),
    MAURITANIA("Mauritania"),
    MAURITIUS("Mauritius"),
    MEXICO("Mexico"),
    MOLDOVA("Moldova"),
    MONGOLIA("Mongolia"),
    MONTENEGRO("Montenegro"),
    MOROCCO("Morocco"),
    MOZAMBIQUE("Mozambique"),
    MYANMAR("Myanmar"),
    NAMIBIA("Namibia"),
    NEPAL("Nepal"),
    NETHERLANDS("Netherlands"),
    NEW_ZEALAND("New Zealand"),
    NICARAGUA("Nicaragua"),
    NIGER("Niger"),
    NIGERIA("Nigeria"),
    NORTH_KOREA("North Korea"),
    NORTH_MACEDONIA("North Macedonia"),
    NORWAY("Norway"),
    OMAN("Oman"),
    PAKISTAN("Pakistan"),
    PALESTINE("Palestine"),
    PANAMA("Panama"),
    PARAGUAY("Paraguay"),
    PERU("Peru"),
    PHILIPPINES("Philippines"),
    POLAND("Poland"),
    PORTUGAL("Portugal"),
    QATAR("Qatar"),
    ROMANIA("Romania"),
    RUSSIA("Russia"),
    RWANDA("Rwanda"),
    SAO_TOME_AND_PRINCIPE("Sao Tome and Principe"),
    SAUDI_ARABIA("Saudi Arabia"),
    SENEGAL("Senegal"),
    SERBIA("Serbia"),
    SEYCHELLES("Seychelles"),
    SIERRA_LEONE("Sierra Leone"),
    SINGAPORE("Singapore"),
    SLOVAKIA("Slovakia"),
    SLOVENIA("Slovenia"),
    SOLOMON_ISLANDS("Solomon Islands"),
    SOMALIA("Somalia"),
    SOUTH_AFRICA("South Africa"),
    SOUTH_KOREA("South Korea"),
    SOUTH_SUDAN("South Sudan"),
    SPAIN("Spain"),
    SRI_LANKA("Sri Lanka"),
    SUDAN("Sudan"),
    SURINAME("Suriname"),
    SWEDEN("Sweden"),
    SWITZERLAND("Switzerland"),
    SYRIA("Syria"),
    TAIWAN("Taiwan"),
    TAJIKISTAN("Tajikistan"),
    TANZANIA("Tanzania"),
    THAILAND("Thailand"),
    TIMOR_LESTE("Timor-Leste"),
    TOGO("Togo"),
    TRINIDAD_AND_TOBAGO("Trinidad and Tobago"),
    TUNISIA("Tunisia"),
    TURKEY("Turkey"),
    TURKMENISTAN("Turkmenistan"),
    UGANDA("Uganda"),
    UKRAINE("Ukraine"),
    UNITED_ARAB_EMIRATES("United Arab Emirates"),
    UNITED_KINGDOM("United Kingdom"),
    UNITED_STATES("United States"),
    URUGUAY("Uruguay"),
    UZBEKISTAN("Uzbekistan"),
    VENEZUELA("Venezuela"),
    VIETNAM("Vietnam"),
    YEMEN("Yemen"),
    ZAMBIA("Zambia"),
    ZIMBABWE("Zimbabwe"),

    CARIBBEAN("Caribbean"),  // Combined class for Caribbean island nations
    PACIFIC_ISLANDS("Pacific Islands");  // Combined class for Pacific island nations

    private final String name;

    public static Country fromString(String name) {
        for (Country country : Country.values()) {
            if (country.getName().equalsIgnoreCase(name)) {
                return country;
            }
        }
        throw new IllegalArgumentException("No enum constant with name " + name);
    }
}
