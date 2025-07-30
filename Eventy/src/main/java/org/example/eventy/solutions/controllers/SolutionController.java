package org.example.eventy.solutions.controllers;

import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.UnitValue;
import org.example.eventy.common.models.PagedResponse;
import org.example.eventy.solutions.dtos.PricelistItemDTO;
import org.example.eventy.solutions.dtos.SolutionCardDTO;
import org.example.eventy.solutions.dtos.SolutionDetailsDTO;
import org.example.eventy.solutions.dtos.services.ServiceDTO;
import org.example.eventy.solutions.dtos.services.UpdateServiceDTO;
import org.example.eventy.solutions.models.*;
import org.example.eventy.solutions.services.*;
import org.example.eventy.users.models.User;
import org.example.eventy.users.services.UserService;
import org.example.eventy.util.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/solutions")
public class SolutionController {

    @Autowired
    private SolutionService solutionService;
    @Autowired
    private UserService userService;
    @Autowired
    private TokenUtils tokenUtils;
    @Autowired
    private SolutionHistoryService solutionHistoryService;
    @Autowired
    private ServiceService serviceService;
    @Autowired
    private ProductService productService;

    @GetMapping(value = "/favorite/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PagedResponse<SolutionCardDTO>> getFavoriteSolutions(@PathVariable Long userId, @RequestParam(required = false, defaultValue = "") String search,
                                                                               @RequestHeader(value = "Authorization", required = false) String token, Pageable pageable) {
        User user = null;
        if(token != null) {
            token = token.substring(7);

            try {
                user = userService.findByEmail(tokenUtils.getUsernameFromToken(token));
            }
            catch (Exception ignored) {
            }
        }
        User finalUser = user;

        List<Solution> providerSolutions = solutionService.getFavoriteSolutionsByUser(userId, search, pageable);

        List<SolutionCardDTO> solutionCards = providerSolutions.stream().map(solution -> new SolutionCardDTO(solution, finalUser)).collect(Collectors.toList());
        long count = solutionService.getFavoriteSolutionsByUserCount(userId);
        PagedResponse<SolutionCardDTO> response = new PagedResponse<>(solutionCards, (int) Math.ceil((double) count / pageable.getPageSize()), count);
        return new ResponseEntity<PagedResponse<SolutionCardDTO>>(response, HttpStatus.OK);
    }

    @PutMapping(value = "/favorite/{solutionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Boolean> toggleFavorite(@PathVariable Long solutionId, @RequestHeader(value = "Authorization", required = false) String token) {
        Solution solution = solutionService.getSolution(solutionId);

        if(solution == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (token == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        User user;
        try {
            token = token.substring(7);
            user = userService.findByEmail(tokenUtils.getUsernameFromToken(token));

            if(user == null) {
                throw new Exception();
            }
        }
        catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        userService.toggleFavoriteSolution(user, solution);

        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @GetMapping(value = "/catalog/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PagedResponse<SolutionCardDTO>> getProviderCatalog(@PathVariable Long userId, @RequestParam(required = false, defaultValue = "") String search,
                                                                             @RequestHeader(value = "Authorization", required = false) String token, Pageable pageable) {
        User user = null;
        if(token != null) {
            token = token.substring(7);

            try {
                user = userService.findByEmail(tokenUtils.getUsernameFromToken(token));
            }
            catch (Exception ignored) {
            }
        }
        User finalUser = user;

        List<Solution> providerSolutions = solutionService.getSolutionsByProvider(userId, search, pageable);

        List<SolutionCardDTO> solutionCards = providerSolutions.stream().map(solution -> new SolutionCardDTO(solution, finalUser)).collect(Collectors.toList());
        long count = solutionService.getSolutionsByProviderCount(userId);
        PagedResponse<SolutionCardDTO> response = new PagedResponse<>(solutionCards, (int) Math.ceil((double) count / pageable.getPageSize()), count);
        return new ResponseEntity<PagedResponse<SolutionCardDTO>>(response, HttpStatus.OK);
    }

    /* this returns SolutionCardDTOs, because there is NO CASE where:
       1) we need ALL solution
       2) they are NOT in card shapes (they always will be if we are getting all solution) */
    // GET http://localhost:8080/api/solutions?search=party&type=Service&categories=Music,Decoration&eventTypes=Wedding,Birthday&company=EventCorp&minPrice=100&maxPrice=5000&startDate=2024-12-20T10:00:00&endDate=2024-12-20T20:00:00&isAvailable=true&page=0&size=10&sort=name,asc
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PagedResponse<SolutionCardDTO>> getSolutions(
            @RequestParam(required = false, defaultValue = "") String search,
            @RequestParam(required = false, defaultValue = "Any") String type,
            @RequestParam(required = false) ArrayList<String> categories,
            @RequestParam(required = false) ArrayList<String> eventTypes,
            @RequestParam(required = false, defaultValue = "") String company,
            @RequestParam(required = false, defaultValue = "0") Double minPrice,
            @RequestParam(required = false, defaultValue = "99999999") Double maxPrice,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(required = false, defaultValue = "true") Boolean isAvailable,
            Pageable pageable,
            @RequestHeader(value = "Authorization", required = false) String token) {
        // Pageable - page, size, sort
        // sort by: "category", "name", "price,asc", "price,desc"
        if (startDate == null) {
            startDate = LocalDateTime.of(1970, 1, 1, 0, 0); // Very small date
        } else {
            startDate = LocalDateTime.of(startDate.getYear(), startDate.getMonth(), startDate.getDayOfMonth(), 0, 0, 0).plusDays(1);
        }
        if (endDate == null) {
            endDate = LocalDateTime.of(2099, 12, 31, 23, 59); // Very large date
        } else {
            endDate = LocalDateTime.of(endDate.getYear(), endDate.getMonth(), endDate.getDayOfMonth(), 23, 59, 59).plusDays(1);
        }

        StringBuilder eventTypesConcatenated = new StringBuilder();
        if (eventTypes != null && !eventTypes.isEmpty()) {
            for (String et : eventTypes) {
                eventTypesConcatenated.append(et).append(",");
            }
            eventTypesConcatenated.deleteCharAt(eventTypesConcatenated.length() - 1);
        }

        StringBuilder categories2 = new StringBuilder();
        if (categories != null) {
            for (String c : categories) {
                categories2.append(c).append(",");
            }
            categories2.deleteCharAt(categories2.length() - 1);
        }

        User user = null;
        if(token != null) {
            token = token.substring(7);

            try {
                user = userService.findByEmail(tokenUtils.getUsernameFromToken(token));
            }
            catch (Exception ignored) {
            }
        }

        Page<Solution> solutions = solutionService.getSolutions(search, type, categories2.toString(), eventTypesConcatenated.toString(), company, minPrice, maxPrice, startDate, endDate, isAvailable, user, pageable);

        List<SolutionCardDTO> solutionCardsDTO = new ArrayList<>();
        for (Solution solution : solutions) {
            solutionCardsDTO.add(new SolutionCardDTO(solution, user));
        }
        long count = solutions.getTotalElements();

        PagedResponse<SolutionCardDTO> response = new PagedResponse<>(solutionCardsDTO, (int) Math.ceil((double) count / pageable.getPageSize()), count);
        return new ResponseEntity<PagedResponse<SolutionCardDTO>>(response, HttpStatus.OK);
    }

    // GET "/api/solutions/cards/5"
    @GetMapping(value = "/cards/{solutionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('Organizer')")
    public ResponseEntity<SolutionCardDTO> getSolutionCard(@PathVariable Long solutionId, @RequestHeader(value = "Authorization", required = false) String token) {
        Solution solution = solutionService.getSolution(solutionId);

        User user = null;
        if(token != null) {
            token = token.substring(7);

            try {
                user = userService.findByEmail(tokenUtils.getUsernameFromToken(token));
            }
            catch (Exception ignored) {
            }
        }
        if (solution != null) {
            SolutionCardDTO solutionCard = new SolutionCardDTO(solution, user);
            return new ResponseEntity<SolutionCardDTO>(solutionCard, HttpStatus.OK);
        }

        return new ResponseEntity<SolutionCardDTO>(HttpStatus.NOT_FOUND);
    }

    // GET "/api/solutions/featured"
    @GetMapping(value = "/featured", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<SolutionCardDTO>> getFeaturedSolutions(@RequestHeader(value = "Authorization", required = false) String token) {
        User user = null;
        if(token != null) {
            token = token.substring(7);

            try {
                user = userService.findByEmail(tokenUtils.getUsernameFromToken(token));
            }
            catch (Exception ignored) {
            }
        }

        ArrayList<Solution> featuredSolutions = solutionService.getFeaturedSolutions(user);

        ArrayList<SolutionCardDTO> featuredSolutionsDTO = new ArrayList<>();
        for (Solution solution : featuredSolutions) {
            featuredSolutionsDTO.add(new SolutionCardDTO(solution, user));
        }

        return new ResponseEntity<Collection<SolutionCardDTO>>(featuredSolutionsDTO, HttpStatus.OK);
    }

    @GetMapping(value = "/pricelist", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('Provider')")
    public ResponseEntity<PagedResponse<PricelistItemDTO>> getProviderPrices(@RequestHeader(value = "Authorization") String token, Pageable pageable) {
        User user = null;
        if(token != null) {
            token = token.substring(7);

            try {
                user = userService.findByEmail(tokenUtils.getUsernameFromToken(token));
            }
            catch (Exception ignored) {
            }
        }

        if (user == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        List<Solution> solutions = solutionService.getSolutionsByProvider(user.getId(), "", pageable);
        long count = solutionService.getSolutionsByProviderCount(user.getId());
        List<PricelistItemDTO> items = solutions.stream().map(solution -> new PricelistItemDTO(solution.getId(), solution.getName(), solution.getPrice(), (double) solution.getDiscount())).toList();
        PagedResponse<PricelistItemDTO> response = new PagedResponse<>(items, (int) Math.ceil((double) count / pageable.getPageSize()), count);
        return new ResponseEntity<PagedResponse<PricelistItemDTO>>(response, HttpStatus.OK);
    }

    @PutMapping(value = "/pricelist", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('Provider')")
    public ResponseEntity<PricelistItemDTO> updatePrice(@RequestHeader(value = "Authorization") String token, @RequestBody PricelistItemDTO newData) {
        User user = null;
        if(token != null) {
            token = token.substring(7);

            try {
                user = userService.findByEmail(tokenUtils.getUsernameFromToken(token));
            }
            catch (Exception ignored) {
            }
        }

        Solution solution = solutionService.getSolution(newData.getId());
        if (solution == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (solution.getPrice() == newData.getPrice() && solution.getDiscount() == newData.getDiscount()) {
            return new ResponseEntity<PricelistItemDTO>(new PricelistItemDTO(solution.getId(), solution.getName(), solution.getPrice(), (double) solution.getDiscount()) ,HttpStatus.OK);
        }

        if (solution instanceof Service) {
            Service service = (Service) solution;
            service = serviceService.updatePrice(service, newData.getPrice(), newData.getDiscount());
            return new ResponseEntity<PricelistItemDTO>(new PricelistItemDTO(service.getId(), service.getName(), service.getPrice(), (double) service.getDiscount()), HttpStatus.OK);
        } else {
            Product product = (Product) solution;
            product.setPrice(newData.getPrice());
            product.setDiscount(newData.getDiscount().intValue());
            product.setCurrentProduct(solutionHistoryService.save(new SolutionHistory(product)));
            product = productService.save(product);
            return new ResponseEntity<PricelistItemDTO>(new PricelistItemDTO(product.getId(), product.getName(), product.getPrice(), (double) product.getDiscount()), HttpStatus.OK);
        }
    }

    @GetMapping(value = "/event-types", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<String>> getAllUniqueEventTypesForSolutions() {
        ArrayList<String> eventTypeNames = solutionService.getAllUniqueEventTypesForSolutions();
        return new ResponseEntity<Collection<String>>(eventTypeNames, HttpStatus.OK);
    }

    @GetMapping(value = "/categories", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<String>> getAllUniqueCategoriesForSolutions() {
        ArrayList<String> categoryNames = solutionService.getAllUniqueCategoriesForSolutions();
        return new ResponseEntity<Collection<String>>(categoryNames, HttpStatus.OK);
    }

    @GetMapping(value = "/companies", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<String>> getAllUniqueCompaniesForSolutions() {
        ArrayList<String> companyNames = solutionService.getAllUniqueCompaniesForSolutions();
        return new ResponseEntity<Collection<String>>(companyNames, HttpStatus.OK);
    }

    @PutMapping(value = "/{solutionId}/availability")
    @PreAuthorize("hasRole('Provider')")
    public ResponseEntity<?> toggleAvailability(@PathVariable Long solutionId) {
        if(solutionService.toggleAvailability(solutionId) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping(value = "/{solutionId}/visibility")
    @PreAuthorize("hasRole('Provider')")
    public ResponseEntity<?> toggleVisibility(@PathVariable Long solutionId) {
        if(solutionService.toggleVisible(solutionId) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/{solutionId}")
    @PreAuthorize("hasRole('Provider')")
    public ResponseEntity<?> deleteSolution(@PathVariable Long solutionId) {
        if(solutionService.delete(solutionId) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SolutionDetailsDTO> getSolution(@PathVariable Long id, @RequestHeader(value = "Authorization", required = false) String token) {
        Solution solution = solutionService.getSolution(id);
        if (solution == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        User user = null;
        if(token != null) {
            token = token.substring(7);

            try {
                user = userService.findByEmail(tokenUtils.getUsernameFromToken(token));
            }
            catch (Exception ignored) {
            }
        }

        if (user != null && user.getBlocked().contains(solution.getProvider())) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity<>(new SolutionDetailsDTO(solution, user), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('Provider')")
    @GetMapping(value = "/pricelist/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> downloadPricelistPDF(@RequestHeader (value = "Authorization") String token) {
        User user = null;
        if(token != null) {
            token = token.substring(7);

            try {
                user = userService.findByEmail(tokenUtils.getUsernameFromToken(token));
            }
            catch (Exception ignored) {
            }
        }

        if (user == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        List<Solution> solutions = solutionService.getSolutionsByProviderUnpaged(user.getId());
        if (solutions.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }


        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            PdfWriter pdfWriter = new PdfWriter(byteArrayOutputStream);

            com.itextpdf.kernel.pdf.PdfDocument pdfDocument = new com.itextpdf.kernel.pdf.PdfDocument(pdfWriter);
            Document document = new Document(pdfDocument);
            document.setFont(PdfFontFactory.createFont("Helvetica-Bold"));

            document.add(new Paragraph("Pricelist").setFontSize(18).setBold().setMarginTop(20));

            Table table = new Table(new float[]{1, 4, 2, 2, 2});
            table.setWidth(UnitValue.createPercentValue(100));

            table.addHeaderCell(new Cell().add(new Paragraph("No.").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("Name").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("Price").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("Discount").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("Final Price").setBold()));

            int count = 1;
            for (Solution solution : solutions) {
                table.addCell(String.valueOf(count++));
                table.addCell(solution.getName());
                table.addCell(String.format("%.2f", solution.getPrice()));
                table.addCell(String.format("%.2f", (double) solution.getDiscount()));
                table.addCell(String.format("%.2f", solution.getPrice() - (solution.getDiscount() * solution.getPrice() / 100)));
            }

            document.add(table);
            document.close();

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=pricelist.pdf");
            headers.add(HttpHeaders.CONTENT_TYPE, "application/pdf");

            return new ResponseEntity<>(byteArrayOutputStream.toByteArray(), headers, HttpStatus.OK);
        }
        catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
