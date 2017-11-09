package com.navjot.deepak.manpreet.pdfsociety.Models;

public class Pdf {

    private String pdfname;
    private String description;
    private String uid;
    private int no_of_downloads;
    private String download_url;
    private String username;
    private double pdfsize;
    private String uploaddate;

    Pdf() {
    }

    public Pdf(String pdfname, String description, String uid, int no_of_downloads, String download_url, String username, double pdfsize, String uploaddate) {
        this.pdfname = pdfname;
        this.description = description;
        this.uid = uid;
        this.no_of_downloads = no_of_downloads;
        this.download_url = download_url;
        this.username = username;
        this.pdfsize = pdfsize;
        this.uploaddate = uploaddate;
    }

    public String getPdfname() {
        return pdfname;
    }

    public void setPdfname(String pdfname) {
        this.pdfname = pdfname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getNo_of_downloads() {
        return no_of_downloads;
    }

    public void setNo_of_downloads(int no_of_downloads) {
        this.no_of_downloads = no_of_downloads;
    }

    public String getDownload_url() {
        return download_url;
    }

    public void setDownload_url(String download_url) {
        this.download_url = download_url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public double getPdfsize() {
        return pdfsize;
    }

    public void setPdfsize(double pdfsize) {
        this.pdfsize = pdfsize;
    }

    public String getUploaddate() {
        return uploaddate;
    }

    public void setUploaddate(String uploaddate) {
        this.uploaddate = uploaddate;
    }

    @Override
    public String toString() {
        return "Pdf{" +
                "pdfname='" + pdfname + '\'' +
                ", description='" + description + '\'' +
                ", uid='" + uid + '\'' +
                ", no_of_downloads=" + no_of_downloads +
                ", download_url='" + download_url + '\'' +
                ", username='" + username + '\'' +
                ", pdfsize=" + pdfsize +
                ", uploaddate='" + uploaddate + '\'' +
                '}';
    }
}
