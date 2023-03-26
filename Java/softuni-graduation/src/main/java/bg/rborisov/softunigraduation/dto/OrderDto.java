package bg.rborisov.softunigraduation.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto implements Serializable {

    private String orderStatus;

    private String orderNumber;

    private String firstName;

    private String lastName;

    private String email;

    private String country;

    private String city;

    private String address;
}