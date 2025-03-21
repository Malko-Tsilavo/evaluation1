package site.easy.to.build.crm.google.service.calendar;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.http.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.easy.to.build.crm.entity.Lead;
import site.easy.to.build.crm.entity.OAuthUser;
import site.easy.to.build.crm.google.model.calendar.*;
import site.easy.to.build.crm.google.util.GoogleApiHelper;
import site.easy.to.build.crm.service.lead.LeadService;
import site.easy.to.build.crm.service.user.OAuthUserService;
import site.easy.to.build.crm.google.util.TimeDateUtil;


import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class GoogleCalendarApiServiceImpl implements GoogleCalendarApiService {

    private static final String API_BASE_URL = "https://www.googleapis.com/calendar/v3/calendars/";

    private final OAuthUserService oAuthUserService;
    private final ObjectMapper objectMapper;
    private final LeadService leadService;

    @Autowired
    public GoogleCalendarApiServiceImpl(OAuthUserService oAuthUserService, ObjectMapper objectMapper, LeadService leadService) {
        this.oAuthUserService = oAuthUserService;
        this.objectMapper = objectMapper;
        this.leadService = leadService;
    }

    public EventDisplayList getEvents(String calendarId, OAuthUser oauthUser) throws IOException, GeneralSecurityException {
        String accessToken = oAuthUserService.refreshAccessTokenIfNeeded(oauthUser);

        HttpRequestFactory requestFactory = GoogleApiHelper.createRequestFactory(accessToken);

        GenericUrl eventsUrl = new GenericUrl(API_BASE_URL + calendarId + "/events");

        String nowInRfc3339 = DateTimeFormatter.ISO_INSTANT.format(Instant.now());

        eventsUrl.put("timeMin", nowInRfc3339);
        eventsUrl.put("singleEvents", "true");
        eventsUrl.put("orderBy", "startTime");

        HttpRequest request = requestFactory.buildGetRequest(eventsUrl);
        HttpResponse response = request.execute();
        String jsonResponse = response.parseAsString();
        EventList eventList = objectMapper.readValue(jsonResponse, EventList.class);

        // Get the current month and year for filtering events
        int currentMonth = LocalDate.now().getMonthValue();
        int currentYear = LocalDate.now().getYear();

        // Convert Event objects to EventDisplay objects
        List<EventDisplay> eventDisplays = eventList.getItems().stream()
                .map(event -> {
                    EventDateTime start = event.getStart();
                    EventDateTime end = event.getEnd();

                    // Variables pour gérer la date et l'heure
                    String startDate = null;
                    String startTime = null;
                    String startTimeZone = null;
                    String endDate = null;
                    String endTime = null;
                    String endTimeZone = null;

                    // Gestion du startDate
                    if (start.getDateTime() != null) {
                        // Si dateTime est défini, on l'extrait en date et heure
                        Map<String, String> startDateTimeParts = TimeDateUtil.extractDateTime(start.getDateTime());
                        startDate = startDateTimeParts.get("date");
                        startTime = startDateTimeParts.get("time");
                        startTimeZone = startDateTimeParts.get("timeZone");
                    } else if (start.getDateTime() == null && start.getDate() != null) {
                        // Si dateTime est null et qu'il y a une date, on utilise seulement la date
                        startDate = start.getDate();
                        startTime = "00:00";  // Par défaut, on fixe l'heure à 00:00
                        startTimeZone = start.getTimeZone();

                        // Créer une dateTime à partir de la date (commence à 00:00)
                        String startDateTime = startDate + "T00:00:00" + startTimeZone;
                        start.setDateTime(startDateTime);  // Mettez à jour le champ dateTime
                    }

                    // Gestion du endDate
                    if (end.getDateTime() != null) {
                        // Si dateTime est défini, on l'extrait en date et heure
                        Map<String, String> endDateTimeParts = TimeDateUtil.extractDateTime(end.getDateTime());
                        endDate = endDateTimeParts.get("date");
                        endTime = endDateTimeParts.get("time");
                        endTimeZone = endDateTimeParts.get("timeZone");
                    } else if (end.getDateTime() == null && end.getDate() != null) {
                        // Si dateTime est null et qu'il y a une date, on utilise seulement la date
                        endDate = end.getDate();
                        endTime = "00:00";  // Par défaut, on fixe l'heure à 00:00
                        endTimeZone = end.getTimeZone();

                        // Créer une dateTime à partir de la date (commence à 00:00) jusqu'au lendemain à 00:00
                        String endDateTime = endDate + "T00:00:00" + endTimeZone;
                        end.setDateTime(endDateTime);  // Mettez à jour le champ dateTime
                    }

                    // Vérifier qu'on a au moins une date de début et une date de fin
                    if (startDate == null || endDate == null) {
                        return null;  // Ignorer l'événement s'il manque une date
                    }

                    return new EventDisplay(
                            event.getId(),
                            event.getSummary(),
                            startDate,
                            startTime,
                            endDate,
                            endTime,
                            startTimeZone,
                            event.getAttendees()
                    );
                })
                .filter(Objects::nonNull)  // On filtre les événements nuls (ignorés)
                .collect(Collectors.toList());

        // Print the events of the current month
        System.out.println("Événements du mois actuel : ");
        eventDisplays.stream()
                .filter(event -> {
                    // Vérifier si la date de début est dans le mois et l'année actuels
                    LocalDate startLocalDate = LocalDate.parse(event.getStartDate());
                    return startLocalDate.getMonthValue() == currentMonth && startLocalDate.getYear() == currentYear;
                })
                .forEach(event -> {
                    // Afficher les événements du mois actuel
                    System.out.println("ID: " + event.getId());
                    System.out.println("Résumé: " + event.getSummary());
                    System.out.println("Date de début: " + event.getStartDate());
                    System.out.println("Heure de début: " + event.getStartTime());
                    System.out.println("Date de fin: " + event.getEndDate());
                    System.out.println("Heure de fin: " + event.getEndTime());
                    System.out.println("=================================");
                });

        // Retourner la liste des événements affichables
        return new EventDisplayList(eventDisplays);
    }

    
            
    public Event updateEvent(String calendarId, OAuthUser oauthUser, String eventId, Event updatedEvent) throws IOException, GeneralSecurityException {
        String accessToken = oAuthUserService.refreshAccessTokenIfNeeded(oauthUser);

        updateLead(oauthUser, eventId, "Meeting updated");

        HttpRequestFactory requestFactory = GoogleApiHelper.createRequestFactory(accessToken);

        GenericUrl updateEventUrl = new GenericUrl(API_BASE_URL + calendarId + "/events/" + eventId + "?sendUpdates=all");
        String eventJson = objectMapper.writeValueAsString(updatedEvent);

        HttpContent content = new ByteArrayContent("application/json", eventJson.getBytes(StandardCharsets.UTF_8));
        HttpRequest request = requestFactory.buildPutRequest(updateEventUrl, content);
        HttpResponse response = request.execute();

        String jsonResponse = response.parseAsString();
        return objectMapper.readValue(jsonResponse, Event.class);
    }

    public void deleteEvent(String calendarId, OAuthUser oauthUser, String eventId) throws IOException, GeneralSecurityException {
        String accessToken = oAuthUserService.refreshAccessTokenIfNeeded(oauthUser);

        updateLead(oauthUser, eventId, "Meeting canceled");

        HttpRequestFactory requestFactory = GoogleApiHelper.createRequestFactory(accessToken);

        GenericUrl deleteEventUrl = new GenericUrl(API_BASE_URL + calendarId + "/events/" + eventId + "?sendUpdates=all");
        HttpRequest request = requestFactory.buildDeleteRequest(deleteEventUrl);
        HttpResponse response = request.execute();
    }

    public EventDisplay getEvent(String calendarId, OAuthUser oauthUser, String eventId) throws IOException, GeneralSecurityException {
        // Récupérer le token d'accès
        String accessToken = oAuthUserService.refreshAccessTokenIfNeeded(oauthUser);
    
        // Créer la requête HTTP pour récupérer l'événement spécifique
        HttpRequestFactory requestFactory = GoogleApiHelper.createRequestFactory(accessToken);
        GenericUrl eventUrl = new GenericUrl(API_BASE_URL + calendarId + "/events/" + eventId);
        
        HttpRequest request = requestFactory.buildGetRequest(eventUrl);
        HttpResponse response = request.execute();
        
        // Parser la réponse JSON dans un objet Event
        String jsonResponse = response.parseAsString();
        Event event = objectMapper.readValue(jsonResponse, Event.class);
        
        // Extraire les informations de date et heure de début et de fin
        EventDateTime start = event.getStart();
        EventDateTime end = event.getEnd();
        
        // Initialiser les variables pour les dates et heures
        String startDate = null, startTime = null, startTimeZone = null;
        String endDate = null, endTime = null, endTimeZone = null;
    
        // Gérer la date et l'heure de début
        if (start != null && start.getDateTime() != null) {
            Map<String, String> startDateTimeParts = TimeDateUtil.extractDateTime(start.getDateTime());
            startDate = startDateTimeParts.get("date");
            startTime = startDateTimeParts.get("time");
            startTimeZone = startDateTimeParts.get("timeZone");
        } else if (start != null && start.getDate() != null) {
            // Si startDateTime est null, utiliser la date uniquement
            startDate = start.getDate();
            startTime = "00:00"; // Fixer l'heure à 00:00 si la date seule est présente
            startTimeZone = start.getTimeZone();
        }
    
        // Gérer la date et l'heure de fin
        if (end != null && end.getDateTime() != null) {
            Map<String, String> endDateTimeParts = TimeDateUtil.extractDateTime(end.getDateTime());
            endDate = endDateTimeParts.get("date");
            endTime = endDateTimeParts.get("time");
            endTimeZone = endDateTimeParts.get("timeZone");
        } else if (end != null && end.getDate() != null) {
            // Si endDateTime est null, utiliser la date uniquement
            endDate = end.getDate();
            endTime = "00:00"; // Fixer l'heure à 00:00 si la date seule est présente
            endTimeZone = end.getTimeZone();
        }
    
        // Si la date de début ou la date de fin est manquante, on retourne null pour éviter les erreurs
        if (startDate == null || endDate == null) {
            return null; // L'événement est ignoré si une des dates est manquante
        }
    
        // Créer et retourner l'objet EventDisplay avec les informations extraites
        return new EventDisplay(
                event.getId(),
                event.getSummary(),
                startDate,
                startTime,
                endDate,
                endTime,
                startTimeZone,
                event.getAttendees()
        );
    }
    
    private void updateLead(OAuthUser oAuthUser, String eventId, String status) {
        Lead lead = leadService.findByMeetingId(eventId);
        if (lead != null) {
            lead.setEmployee(oAuthUser.getUser());
            lead.setStatus(status);
            if(status.equals("Meeting canceled")){
                lead.setMeetingId("");
            } else {
                lead.setMeetingId(eventId);
            }
            leadService.save(lead);
        }
    }

    @Override
    public String createEvent(String calendarId, OAuthUser oauthUser, Event event)
            throws IOException, GeneralSecurityException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createEvent'");
    }

}
