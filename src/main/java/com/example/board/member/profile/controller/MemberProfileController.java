package com.example.board.member.profile.controller;

import com.example.board.member.commons.response.ApiResponse;
import com.example.board.member.profile.controller.dto.request.MemberProfileCreateRequest;
import com.example.board.member.profile.controller.dto.request.MemberProfileUpdateRequest;
import com.example.board.member.profile.controller.dto.response.MemberProfileGetResponse;
import com.example.board.member.profile.controller.dto.response.MemberProfileNicknameCheckResponse;
import com.example.board.member.profile.service.MemberProfileService;
import com.example.board.member.profile.service.command.MemberProfileCreateCommand;
import com.example.board.member.profile.service.command.MemberProfileUpdateCommand;
import com.example.board.member.profile.service.result.MemberProfileResult;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.example.board.member.commons.response.MemberProfileErrorCode.*;
import static com.example.board.member.commons.response.MemberProfileSuccessCode.*;
import static com.example.board.member.commons.utils.ResponseUtils.errorResponse;
import static com.example.board.member.commons.utils.ResponseUtils.successResponse;

@Validated
@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberProfileController {
    private final MemberProfileService memberProfileService;

    @PutMapping("/{member-id}/profile")
    public ResponseEntity<ApiResponse<Void>> createProfile(@PathVariable("member-id") Long id, @Valid @RequestBody MemberProfileCreateRequest request) {
        var result = memberProfileService.createProfileIfNotExists(id, new MemberProfileCreateCommand(request.username(), request.nickname()));

        return switch (result) {
            case MemberProfileResult.Create.Success _ -> successResponse(PROFILE_CREATED);
            case MemberProfileResult.Create.AlreadyExists _ -> successResponse(PROFILE_FOUND);
            case MemberProfileResult.Create.HandleDuplicate _ -> errorResponse(HANDLE_DUPLICATED);
            case MemberProfileResult.Create.NicknameDuplicate _ -> errorResponse(NICKNAME_DUPLICATED);
        };
    }

    @GetMapping("/nickname/availability")
    public ResponseEntity<ApiResponse<MemberProfileNicknameCheckResponse>> checkNicknameAvailability(
            @RequestParam
            @NotBlank(message = "닉네임을 입력해주세요.")
            @Size(min = 2, max = 20, message = "닉네임은 2~20자입니다.")
            @Pattern(
                    regexp = "^[a-z0-9가-힣]+$",
                    message = "닉네임은 한글, 영문 소문자, 숫자만 사용할 수 있습니다."
            )
            String nickname) {
        var result = memberProfileService.checkNicknameAvailability(nickname);

        return switch (result) {
            case MemberProfileResult.CheckNickname.Available(var message) -> successResponse(NICKNAME_AVAILABILITY_CHECKED, MemberProfileNicknameCheckResponse.available(message));
            case MemberProfileResult.CheckNickname.Used(var message) -> successResponse(NICKNAME_AVAILABILITY_CHECKED, MemberProfileNicknameCheckResponse.used(message));
        };
    }

    @GetMapping("/{handle}")
    public ResponseEntity<ApiResponse<MemberProfileGetResponse>> getProfile(@PathVariable String handle) {
        var result = memberProfileService.getProfile(handle);

        return switch (result) {
            case MemberProfileResult.Get.Success(var nickname) -> successResponse(PROFILE_FOUND, new MemberProfileGetResponse(nickname));
            case MemberProfileResult.Get.NotFound _ -> errorResponse(PROFILE_NOT_FOUND);
        };
    }

    @PatchMapping("/me/profile")
    public ResponseEntity<ApiResponse<Void>> updateProfile(Long id, @RequestBody MemberProfileUpdateRequest request) {
        var result = memberProfileService.updateProfile(id, new MemberProfileUpdateCommand(request.nickname()));

        return switch (result) {
            case MemberProfileResult.Update.Success() -> successResponse(PROFILE_UPDATED);
            case MemberProfileResult.Update.NotFound _ -> errorResponse(PROFILE_NOT_FOUND);
            case MemberProfileResult.Update.NicknameDuplicate _ -> errorResponse(NICKNAME_DUPLICATED);
        };
    }

    @DeleteMapping("/{member-id}/profile")
    public ResponseEntity<ApiResponse<Void>> deleteProfile(@PathVariable("member-id") Long id) {
        var result = memberProfileService.deleteProfile(id);

        return switch (result) {
            case MemberProfileResult.Delete.Success _ -> successResponse(PROFILE_DELETED);
        };
    }
}
