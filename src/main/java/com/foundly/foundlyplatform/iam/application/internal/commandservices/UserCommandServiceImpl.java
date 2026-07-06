package com.foundly.foundlyplatform.iam.application.internal.commandservices;

import com.foundly.foundlyplatform.iam.application.commandservices.UserCommandService;
import com.foundly.foundlyplatform.iam.application.internal.outboundservices.hashing.HashingService;
import com.foundly.foundlyplatform.iam.application.internal.outboundservices.tokens.TokenService;
import com.foundly.foundlyplatform.iam.domain.model.aggregates.User;
import com.foundly.foundlyplatform.iam.domain.model.commands.SignInCommand;
import com.foundly.foundlyplatform.iam.domain.model.commands.SignUpCommand;
import com.foundly.foundlyplatform.iam.domain.repositories.RoleRepository;
import com.foundly.foundlyplatform.iam.domain.repositories.UserRepository;
import com.foundly.foundlyplatform.shared.application.result.ApplicationError;
import com.foundly.foundlyplatform.shared.application.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserCommandServiceImpl implements UserCommandService {
    private final UserRepository userRepository;
    private final HashingService hashingService;
    private final TokenService tokenService;
    private final RoleRepository roleRepository;

    public UserCommandServiceImpl(UserRepository userRepository,
                                  HashingService hashingService,
                                  TokenService tokenService,
                                  RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.hashingService = hashingService;
        this.tokenService = tokenService;
        this.roleRepository = roleRepository;
    }

    @Override
    public Result<ImmutablePair<User, String>, ApplicationError> handle(SignInCommand command) {
        log.info("🔐 Intentando login para: {}", command.username());

        // ✅ CAMBIO IMPORTANTE: Buscar por EMAIL
        var user = userRepository.findByEmail(command.username());

        if (user.isEmpty()) {
            log.warn("❌ Usuario no encontrado: {}", command.username());
            return Result.failure(ApplicationError.notFound("User", command.username()));
        }
        if (!hashingService.matches(command.password(), user.get().getPassword())) {
            log.warn("❌ Contraseña incorrecta para: {}", command.username());
            return Result.failure(ApplicationError.validationError("credentials", "Invalid username or password"));
        }
        var token = tokenService.generateToken(user.get().getUsername());
        log.info("✅ Login exitoso para: {}", command.username());
        return Result.success(ImmutablePair.of(user.get(), token));
    }

    @Override
    public Result<User, ApplicationError> handle(SignUpCommand command) {
        log.info("📝 Registrando usuario: username={}, email={}", command.username(), command.email());

        if (userRepository.existsByUsername(command.username())) {
            log.warn("❌ Username ya existe: {}", command.username());
            return Result.failure(ApplicationError.conflict("User", "Username already exists"));
        }

        // ✅ También validar que el email no exista
        if (userRepository.findByEmail(command.email()).isPresent()) {
            log.warn("❌ Email ya existe: {}", command.email());
            return Result.failure(ApplicationError.conflict("User", "Email already exists"));
        }

        var roles = command.roles().stream()
                .map(role -> roleRepository.findByName(role.getName()))
                .toList();

        if (roles.stream().anyMatch(java.util.Optional::isEmpty)) {
            log.warn("❌ Rol no encontrado para: {}", command.username());
            return Result.failure(ApplicationError.notFound("Role", "one or more role names"));
        }

        var resolvedRoles = roles.stream()
                .map(java.util.Optional::get)
                .toList();

        var user = new User(command.username(), hashingService.encode(command.password()), command.email(), resolvedRoles);
        userRepository.save(user);
        log.info("✅ Usuario registrado exitosamente: {}", command.username());

        return userRepository.findByUsername(command.username())
                .<Result<User, ApplicationError>>map(Result::success)
                .orElseGet(() -> {
                    log.error("❌ No se pudo cargar el usuario creado: {}", command.username());
                    return Result.failure(ApplicationError.unexpected("sign-up", "Created user could not be reloaded"));
                });
    }
}