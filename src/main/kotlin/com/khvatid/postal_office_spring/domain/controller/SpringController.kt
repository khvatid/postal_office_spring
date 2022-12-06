package com.khvatid.postal_office_spring.domain.controller

import com.khvatid.postal_office_spring.domain.model.MailModel
import com.khvatid.postal_office_spring.domain.model.PostalOfficeModel
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.Locale


@RestController
@RequestMapping("/")
interface SpringController {

  @GetMapping(value = ["office/{officeId}"])
  @ResponseBody
  fun getPostalOffice(
    @PathVariable("officeId") officeId: String,
    @RequestHeader(value = "Accept-Language", required = false) locale: Locale //= Locale.getDefault()
  ): ResponseEntity<Any>

  @GetMapping(value = ["office/{officeId}/mail/{id}"])
  @ResponseBody
  fun getMail(
    @PathVariable("officeId") officeId: String,
    @PathVariable("id") id: String,
    @RequestHeader(value = "Accept-Language", required = false) locale: Locale //= Locale.getDefault()
    ): ResponseEntity<Any>

  @PostMapping(value = ["office/create"])
  fun postPostalOffice(
    @RequestBody postalOfficeModel: PostalOfficeModel.Request,
    @RequestHeader(value = "Accept-Language", required = false) locale: Locale //= Locale.getDefault()
  ): ResponseEntity<Any>

  @PostMapping(value = ["office/{officeId}/mail/create"])
  fun postMail(
    @PathVariable("officeId") officeId: String,
    @RequestBody mailModel: MailModel.Request,
    @RequestHeader(value = "Accept-Language", required = false) locale: Locale //= Locale.getDefault()
  ): ResponseEntity<Any>

  @PutMapping(value = ["office/{officeId}/update"])
  fun putPostOffice(
    @PathVariable officeId: String,
    @RequestBody officeModel: PostalOfficeModel.Request,
    @RequestHeader(value = "Accept-Language", required = false) locale: Locale //= Locale.getDefault()
  ): ResponseEntity<Any>

  @PutMapping(value = ["office/{officeId}/mail/{mailId}/update"])
  fun putMail(
    @PathVariable("officeId") officeId: String,
    @PathVariable("mailId") mailId: String,
    @RequestBody mailModel: MailModel.Request,
    @RequestHeader(value = "Accept-Language", required = false) locale: Locale //= Locale.getDefault()
  ): ResponseEntity<Any>

  @DeleteMapping(value = ["office/{officeId}/drop"])
  fun deletePostalOffice(
    @PathVariable("officeId") officeId: String,
    @RequestHeader(value = "Accept-Language", required = false) locale: Locale //= Locale.getDefault()
  ): ResponseEntity<Any>

  @DeleteMapping(value = ["office/{officeId}/mail/{id}/drop"])
  fun deleteMail(
    @PathVariable("officeId") officeId: String,
    @PathVariable("id") id: String,
    @RequestHeader(value = "Accept-Language", required = false) locale: Locale //= Locale.getDefault()
  ): ResponseEntity<Any>
}