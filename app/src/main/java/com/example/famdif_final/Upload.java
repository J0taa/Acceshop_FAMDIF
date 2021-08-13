package com.example.famdif_final;

public class Upload {
    private String mName;
    private String mImageUrl;

     public Upload(){

     }

      public Upload(String name, String imageUri){
         if(name.trim().equals("")){
             name="No name";
         }

         mName = name;
         mImageUrl = imageUri;

      }

    public String getmName() {
        return mName;
    }

    public String getmImageUrl() {
         return mImageUrl;
    }

    public void setmImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }
}
