package nl.novi.eventmanager900102055.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class TicketDto {
    private Long id;
    @NotNull
    private UserDto userDto;
    @NotNull
    private EventDto eventDto;
    @NotNull
    private TransactionDto transactionDto;
    @Min(0)
    private Double price;

    public TicketDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserDto getUserDto() {
        return userDto;
    }

    public void setUserDto(UserDto userDto) {
        this.userDto = userDto;
    }

    public EventDto getEventDto() {
        return eventDto;
    }

    public void setEventDto(EventDto eventDto) {
        this.eventDto = eventDto;
    }

    public TransactionDto getTransactionDto() {
        return transactionDto;
    }

    public void setTransactionDto(TransactionDto transactionDto) {
        this.transactionDto = transactionDto;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
