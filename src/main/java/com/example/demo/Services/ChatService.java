package com.example.demo.Services;

import com.example.demo.Models.DocumentMessage;
import com.example.demo.Models.MessageText;
import com.example.demo.Models.User;
import com.example.demo.Repository.IMessageTextRepo;
import com.example.demo.Repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final IMessageTextRepo messageTextRepo;
    private final IUserRepository userRepo;

    public List<MessageText> getMessages(Long receiverId, Long senderId) {
        List<MessageText> all = messageTextRepo.findAll();
        if (all.isEmpty()) {
            return new ArrayList<>();
        }
        return all.stream()
                .filter(message -> (message.getReceiver().getId().equals(receiverId) && message.getSender().getId().equals(senderId)) ||
                        (message.getReceiver().getId().equals(senderId) && message.getSender().getId().equals(receiverId))).toList();
    }

    public MessageText sendMessage(Long receiverId, Long senderId, String text) {
        MessageText messageText1 = new MessageText();
        messageText1.setText(text);

        Optional<User> receiverById = userRepo.findById(receiverId);
        Optional<User> senderById = userRepo.findById(senderId);

        if (receiverById.isEmpty() || senderById.isEmpty()) return null;

        messageText1.setSender(senderById.get());
        messageText1.setReceiver(receiverById.get());
        messageText1.setCreatedAt(new Date());

        return messageTextRepo.save(messageText1);
    }
}
