package hexlet.code.model;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum UserRole {

    ADMIN,
    USER;

//    public List<SimpleGrantedAuthority> getAuthorities() {
//        List<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
//        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
//        return authorities;
//    }



}
