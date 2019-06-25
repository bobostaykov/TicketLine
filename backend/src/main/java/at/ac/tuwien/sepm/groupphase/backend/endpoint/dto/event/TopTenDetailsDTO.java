package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.event;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;
import java.util.Objects;

@ApiModel(value = "TopTenDetailsDTO", description = "a dto to pass for requests for the top ten events")
public class TopTenDetailsDTO {

    @ApiModelProperty(name = "the months that is searched for")
    private List<String> months;
    @ApiModelProperty(name = "the categories that is searched for")
    private List<String> categories;

    public TopTenDetailsDTO() {}

    public TopTenDetailsDTO(List<String> months, List<String> categories) {
        this.months = months;
        this.categories = categories;
    }

    public List<String> getMonths() {
        return months;
    }

    public void setMonths(List<String> months) {
        this.months = months;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public static TopTenDetailsDTOBuilder builder() {
        return new TopTenDetailsDTOBuilder();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TopTenDetailsDTO that = (TopTenDetailsDTO) o;
        return months.equals(that.months) &&
            categories.equals(that.categories);
    }

    @Override
    public int hashCode() {
        return Objects.hash(months, categories);
    }

    @Override
    public String toString() {
        return "TopTenDetailsDTO{" +
            "months=" + months +
            ", categories=" + categories +
            '}';
    }

    public static final class TopTenDetailsDTOBuilder {

        private List<String> months;
        private List<String> categories;

        public TopTenDetailsDTOBuilder months(List<String> months) {
            this.months = months;
            return this;
        }

        public TopTenDetailsDTOBuilder categories(List<String> categories) {
            this.categories = categories;
            return this;
        }

        public TopTenDetailsDTO build() {
            TopTenDetailsDTO details = new TopTenDetailsDTO();
            details.setMonths(months);
            details.setCategories(categories);
            return details;
        }

    }

}
