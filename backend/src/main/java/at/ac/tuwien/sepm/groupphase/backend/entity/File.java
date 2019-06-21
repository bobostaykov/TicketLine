package at.ac.tuwien.sepm.groupphase.backend.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String fileName;

    private String fileType;

    @Lob
    private byte[] data;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public static DBFileBuilder builder() {
        return new DBFileBuilder();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        File dbfile = (File) o;
        return id.equals(dbfile.id) &&
            fileName.equals(dbfile.fileName) &&
            fileType.equals(dbfile.fileType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fileName, fileType);
    }

    @Override
    public String toString() {
        return "File{" +
            "id=" + id +
            ", fileName=" + fileName +
            ", fileType=" + fileType +
            '}';
    }

    public static final class DBFileBuilder {
        private Long id;
        private String fileName;
        private String fileType;
        private byte[] data;

        private DBFileBuilder() {}

        public DBFileBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public DBFileBuilder fileName(String fileName) {
            this.fileName = fileName;
            return this;
        }

        public DBFileBuilder fileType(String fileType) {
            this.fileType = fileType;
            return this;
        }

        public DBFileBuilder data(byte[] data) {
            this.data = data;
            return this;
        }

        public File build() {
            File dbfile = new File();
            dbfile.setId(id);
            dbfile.setFileName(fileName);
            dbfile.setFileType(fileType);
            dbfile.setData(data);
            return dbfile;
        }
    }
}
