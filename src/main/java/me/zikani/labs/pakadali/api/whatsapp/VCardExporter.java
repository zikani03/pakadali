package me.zikani.labs.pakadali.api.whatsapp;

import ezvcard.Ezvcard;
import ezvcard.VCard;
import ezvcard.parameter.TelephoneType;
import ezvcard.property.Organization;

import java.util.HashMap;
import java.util.List;

public class VCardExporter implements MessagesExporter {
    @Override
    public String mediaType() {
        return "text/vcard";
    }

    @Override
    public String export(List<Message> messages) {
        HashMap<String, VCard> phoneToCard = new HashMap<>();
        for(var message : messages) {
            phoneToCard.computeIfAbsent(message.getPhone(), (key) -> {
//                if (!key.trim().startsWith("+") || !key.trim().startsWith("0")) {
//                    return null; // TODO: use proper phone number check and replace this rudimentary check
//                }
                VCard card = new VCard();
                Organization org = new Organization();
                org.setParameter("name", "[Imported by Pakadali]");
                card.addOrganization(org);
                card.setFormattedName(message.getPhone() + " - from " + message.getGroupChatName());
                card.addTelephoneNumber(message.getPhone(), TelephoneType.CELL);
                return card;
            });
        }

        StringBuilder sb = new StringBuilder();
        phoneToCard.values().forEach(card -> {
            sb.append("\n")
                .append(Ezvcard.write(card).go())
                .append("\n");
        });

        return sb.toString();
    }
}
