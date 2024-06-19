package org.example.gruppsex;

import org.example.gruppsex.model.MyUser;
import org.example.gruppsex.model.UserDTO;
import org.example.gruppsex.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = true)
class GruppSexApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    public void testFirstPageWithoutAuthentication () throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:8080/"))
                .andExpect(status().isUnauthorized()); //
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testFirstPageWithAuthentication () throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:8080/")
                        //.with(httpBasic("admin@admin.com", "pass"))
                ).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testValidRegistration () throws Exception {
        UserDTO user = new UserDTO();
        user.setUsername("test@user.com");
        user.setFirstName("test");
        user.setLastName("test2");
        user.setAge("12");
        user.setPassword("test");

        mockMvc.perform(MockMvcRequestBuilders.post("/register").with(csrf())
                //.with(httpBasic("admin@admin.com", "pass"))
                .flashAttr("user", user)).andExpect(status().isOk())
                .andExpect(view().name("regsuc"));

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testInvalidRegistration () throws Exception {
        UserDTO user = new UserDTO();
        user.setUsername("testuser");
        user.setFirstName("test");
        user.setLastName("test2");
        user.setAge("12");
        user.setPassword("test");

        mockMvc.perform(MockMvcRequestBuilders.post("/register").with(csrf())
                //.with(httpBasic("admin@admin.com", "pass"))
                .flashAttr("user", user))
                .andExpect(status().isOk())
                .andExpect(view().name("registrera"));

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testUserDeletionSuccess () throws Exception {
        UserDTO user = new UserDTO();
        user.setUsername("test@test.com");

        MyUser existingUser = new MyUser();
        existingUser.setUsername("test@test.com");
        existingUser.setRole("USER");

        when(userService.getUserByUsername(anyString())).thenReturn(Optional.of(existingUser));
        mockMvc.perform(MockMvcRequestBuilders.post("/deleteuser").with(csrf())
                //.with(httpBasic("admin@admin.com", "pass"))
                .flashAttr("user", user))
                .andExpect(status().isOk())
                .andExpect(view().name("userDeleted"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testUserDeletionFailure () throws Exception {
        UserDTO user = new UserDTO();
        user.setUsername("test@test.com");

        MyUser existingUser = new MyUser();
        existingUser.setUsername("test@test.com");
        existingUser.setRole("ADMIN");

        when(userService.getUserByUsername(anyString())).thenReturn(Optional.of(existingUser));
        mockMvc.perform(MockMvcRequestBuilders.post("/deleteuser").with(csrf())
                        //.with(httpBasic("admin@admin.com", "pass"))
                        .flashAttr("user", user))
                .andExpect(status().isOk())
                .andExpect(view().name("adminCantBeDeleted"));
    }

}
