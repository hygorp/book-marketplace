package org.hygorp.bookmarketplace.resources;

import org.hygorp.bookmarketplace.records.Seller;
import org.hygorp.bookmarketplace.services.SellerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/sellers")
public class SellerResource {
    private final SellerService sellerService;

    public SellerResource(SellerService sellerService) {
        this.sellerService = sellerService;
    }

    @GetMapping("/find-all")
    public ResponseEntity<Page<Seller>> findAll(Pageable pageable) {
        Page<Seller> sellers = sellerService.findAll(pageable);

        return ResponseEntity.ok().body(sellers);
    }
}
