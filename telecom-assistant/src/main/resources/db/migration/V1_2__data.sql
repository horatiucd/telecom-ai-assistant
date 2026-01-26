DO $$
DECLARE
    v_ver INT;
    v_vdf INT;
    v_org INT;
    v_att INT;

    s_voip INT;
    s_eth INT;
    s_tf INT;
    s_mpls INT;
    s_lo INT;
BEGIN
    INSERT INTO Vendors (Name) VALUES ('Verizon') RETURNING Id INTO v_ver;
    INSERT INTO Vendors (Name) VALUES ('Vodafone') RETURNING Id INTO v_vdf;
    INSERT INTO Vendors (Name) VALUES ('Orange') RETURNING Id INTO v_org;
    INSERT INTO Vendors (Name) VALUES ('ATT') RETURNING Id INTO v_att;

    INSERT INTO ServiceTypes (Name) VALUES ('VOIP') RETURNING Id INTO s_voip;
    INSERT INTO ServiceTypes (Name) VALUES ('Ethernet') RETURNING Id INTO s_eth;
    INSERT INTO ServiceTypes (Name) VALUES ('Toll Free') RETURNING Id INTO s_tf;
    INSERT INTO ServiceTypes (Name) VALUES ('MPLS') RETURNING Id INTO s_mpls;
    INSERT INTO ServiceTypes (Name) VALUES ('Local') RETURNING Id INTO s_lo;

    INSERT INTO Invoices (Number, Date, VendorId, ServiceTypeId, Status, Total)
    VALUES ('ver-voip-rev-1', '2025-06-02', v_ver, s_voip, 'UNDER_REVIEW', 151);
    INSERT INTO Invoices (Number, Date, VendorId, ServiceTypeId, Status, Total)
    VALUES ('ver-eth-rev-1', '2025-06-03', v_ver, s_eth, 'UNDER_REVIEW', 240);
    INSERT INTO Invoices (Number, Date, VendorId, ServiceTypeId, Status, Total)
    VALUES ('ver-tf-paid-1', '2025-06-04', v_ver, s_tf, 'PAID', 102.44);
    INSERT INTO Invoices (Number, Date, VendorId, ServiceTypeId, Status, Total)
    VALUES ('ver-mpls-app-1', '2025-06-01', v_ver, s_mpls, 'APPROVED', 42.44);
    INSERT INTO Invoices (Number, Date, VendorId, ServiceTypeId, Status, Total)
    VALUES ('ver-lo-paid-1', '2025-06-05', v_ver, s_lo, 'PAID', 113.44);

    INSERT INTO Invoices (Number, Date, VendorId, ServiceTypeId, Status, Total)
    VALUES ('vdf-lo-paid-1', '2025-06-10', v_vdf, s_lo, 'PAID', 85.44);
    INSERT INTO Invoices (Number, Date, VendorId, ServiceTypeId, Status, Total)
    VALUES ('vdf-mpls-app-1', '2025-05-10', v_vdf, s_mpls, 'APPROVED', 80.44);
    INSERT INTO Invoices (Number, Date, VendorId, ServiceTypeId, Status, Total)
    VALUES ('vdf-tf-rev-1', '2025-05-20', v_vdf, s_tf, 'UNDER_REVIEW', 10.44);

    INSERT INTO Invoices (Number, Date, VendorId, ServiceTypeId, Status, Total)
    VALUES ('org-voip-paid-1', '2025-04-10', v_org, s_voip, 'PAID', 50.81);
    INSERT INTO Invoices (Number, Date, VendorId, ServiceTypeId, Status, Total)
    VALUES ('org-voip-paid-2', '2025-05-10', v_org, s_voip, 'PAID', 50.81);
    INSERT INTO Invoices (Number, Date, VendorId, ServiceTypeId, Status, Total)
    VALUES ('org-voip-paid-3', '2025-06-10', v_org, s_voip, 'PAID', 50.81);

    INSERT INTO Invoices (Number, Date, VendorId, ServiceTypeId, Status, Total)
    VALUES ('att-eth-app-1', '2025-06-13', v_att, s_eth, 'APPROVED', 100);
    INSERT INTO Invoices (Number, Date, VendorId, ServiceTypeId, Status, Total)
    VALUES ('att-mpls-paid-1', '2025-06-20', v_att, s_mpls, 'PAID', 98);
end;
$$;
