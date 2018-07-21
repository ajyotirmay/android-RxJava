package appjoe.wordpress.com.testdemo;

public class Card {
    private String mImageUrl;
    private String mCountryName;
    private int mRank;

    public Card(String imageUrl, String countryName, int rank) {
        mImageUrl = imageUrl;
        mCountryName = countryName;
        mRank = rank;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public String getCountryName() {
        return mCountryName;
    }

    public int getRank() {
        return mRank;
    }
}
